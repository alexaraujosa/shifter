import LN.LNFacade;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Insufficient arguments. Usage: java Main <aluno/diretor> <Optional: file path>");
            return;
        }
        
        if (args[0].equals("aluno")) {
            System.out.println("TODO UI");
            LNFacade lnFacade = new LNFacade();

            System.out.println("Insere o teu numero mecanografico:");
            Scanner s = new Scanner(System.in);
            int num = s.nextInt();
            boolean res = lnFacade.autenticarAluno(num);
            if (res) {
                System.out.println("Autenticado com sucesso.\n Adiciona um aluno:");
                System.out.println("Numero mecanografico:");
                int num2 = s.nextInt();
                System.out.println("Nome:");
                s.nextLine();
                String nome = s.nextLine();
                System.out.println("Estatuto:");
                boolean estatuto = s.nextBoolean();
                System.out.println("A adicionar aluno...");
                lnFacade.adicionarAluno(num2, nome, estatuto);
                boolean foiAdicionado = lnFacade.autenticarAluno(num2);
                if (foiAdicionado) {
                    System.out.println("Aluno adicionado!");
                }
            }
            else System.out.println("Autenticado sem sucesso.");
        }

        if (args[0].equals("diretor")) {
            System.out.println("TODO UI");
            System.out.println("Insira a palavra passe:");
            Scanner s = new Scanner(System.in);
            String palavra = s.nextLine();
            LNFacade lnFacade = new LNFacade();
            boolean res = lnFacade.autenticarDiretor(palavra);
            if (res) { System.out.println("Diretor autenticado!"); }
            else System.out.println("Autenticado sem sucesso.");

            System.out.println("Inserir aluno na lista de inscritos de uma UC:");
            System.out.println("Codigo de UC:");
            String cod = s.nextLine();
            System.out.println("Numero Mecanografico:");
            int num = s.nextInt();
            System.out.println("A adicionar aluno a lista de inscritos...");
            lnFacade.adicionarInscritoUC(cod, num);
            System.out.println("Aluno adicionado a lista de inscritos com sucessso!");
        }
    }
}