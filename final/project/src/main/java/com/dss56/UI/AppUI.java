package com.dss56.UI;

import com.dss56.LN.LNFacade;
import com.dss56.LN.UC.Turno.Turno;
import com.dss56.Util.DSSException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AppUI {
    private final Scanner sc;
    private final LNFacade facade;
    private final Menu mainMenu;
    private final Menu studentMenu;
    private final Menu directorMenu;
    private boolean isAuthenticated = false;
    private int num = Integer.MAX_VALUE;

    public AppUI() throws SQLException {
        this.sc = new Scanner(System.in);
        this.facade = new LNFacade();
        this.mainMenu = new Menu(new String[] {
                "Exit",
                "Connect as Director",
                "Connect as Student"
        });
        this.studentMenu = new Menu(new String[] {
                "Back",
                "Authenticate",
                "Visualize Schedule"
        });
        this.directorMenu = new Menu(new String[] {
                "Back",
                "Authenticate",
                "Import Students",
                "Generate Schedules Automatically",
                "Allocate Student"
        });
    }

    private void connectDirector() {
        this.directorMenu.run();
    }

    private void connectStudent() {
        this.studentMenu.run();
    }

    private void authenticateDirector() {
        System.out.println("Insert your password: ");
        String pass = this.sc.nextLine();
        this.isAuthenticated = this.facade.autenticarDiretor(pass);
        if (!isAuthenticated) {
            System.out.println("Authentication failed! Try again later.");
        }
    }

    private void importStudents() {
        System.out.println("Insert path to the csv.");
        String path = this.sc.nextLine();
        this.facade.importarAlunos(path);
    }

    private void generateSchedulesAutomatically() {
        System.out.println("Starting to auto generate schedules.");
        this.facade.gerarHorario();
        System.out.println("Finished schedules auto generation.");
    }

    private void allocManual() {
        System.out.println("Insert student number to allocate: ");
        int num = this.sc.nextInt();
        this.sc.nextLine(); // scanner clear
        System.out.println("Insert UC code: ");
        String codigo = this.sc.nextLine();
        System.out.println("Insert shift id: ");
        String id = this.sc.nextLine();

        try {
            this.facade.alocarAluno(num, codigo, id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void authenticateStudent() {
        System.out.println("Insert your student number:");
        int tmpNum = this.sc.nextInt();
        this.isAuthenticated = this.facade.autenticarAluno(tmpNum);
        if (!isAuthenticated) {
            System.out.println("Authentication failed! Try again later.");
            return;
        }
        this.num = tmpNum;
    }

    private void visualizeSchedule() {
        List<Turno> turnos = this.facade.consultarHorario(this.num);
        if (turnos.isEmpty()) System.out.println("You don't have a schedule.");
        else {
//            System.out.println("Schedule of a" + this.num);
//            for (Turno turno : turnos) {
//                System.out.println(turno.toString());
//            }
            List<List<String>> rows = new ArrayList<>();
            rows.add(Arrays.asList("Shift", "Day", "Start Time", "End Time", "UC"));
            for (Turno turno : turnos) {
                rows.add(Arrays.asList(turno.getId(), turno.getDia().toString(), turno.getInicio().toString(), turno.getFim().toString(), turno.getCodigoUC()));
            }

            System.out.println("\n" + DSSException.TableUtil.formatAsTable(rows));
        }
    }

    public void run() {
        this.mainMenu.setHandler(0, this::close);
        this.mainMenu.setHandler(1, this::connectDirector);
        this.mainMenu.setHandler(2, this::connectStudent);

        this.studentMenu.setHandler(0, this::close);
        this.studentMenu.setHandler(1, this::authenticateStudent);
        this.studentMenu.setHandler(2, this::visualizeSchedule);
        this.studentMenu.setPreCondition(1, () -> !this.isAuthenticated);
        this.studentMenu.setPreCondition(2, () -> this.isAuthenticated);

        this.directorMenu.setHandler(0, this::close);
        this.directorMenu.setHandler(1, this::authenticateDirector);
        this.directorMenu.setHandler(2, this::importStudents);
        this.directorMenu.setHandler(3, this::generateSchedulesAutomatically);
        this.directorMenu.setHandler(4, this::allocManual);
        this.directorMenu.setPreCondition(1, () -> !this.isAuthenticated);
        this.directorMenu.setPreCondition(2, () -> this.isAuthenticated);
        this.directorMenu.setPreCondition(3, () -> this.isAuthenticated);
        this.directorMenu.setPreCondition(4, () -> this.isAuthenticated);

        this.mainMenu.run();

        System.out.println("Closing AppUI");
    }

    public void close() {
        this.isAuthenticated = false;
        this.num = Integer.MAX_VALUE;
    }
}