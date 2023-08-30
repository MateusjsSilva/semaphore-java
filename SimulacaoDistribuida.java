import java.util.Scanner;

public class SimulacaoDistribuida {

    private static final int NUM_NODES = 3;
    private static final String RECURSO_COMPARTILHADO = "Recurso Compartilhado";
    private static final ThreadGroup threadGroup = new ThreadGroup("MeuGrupoDeThreads");

    public static void main(String[] args) {
        exibirMenu();
    }

    static void exibirMenu() {

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n\nEscolha o cenário: ");
                System.out.println("1 - Vários nós simultâneos (concorrência total)");
                System.out.println("2 - Uso correto do semáforo");
                System.out.println("3 - Uso incorreto do semáforo");
                System.out.println("0 - Sair");
                System.out.print("\nDigite a opção: ");

                int cenario = scanner.nextInt();
                scanner.nextLine();

                System.out.println();

                switch (cenario) {
                    case 0:
                        System.out.println("Encerrando a simulação.");
                        return;
                    case 1:
                        executarCenario1();
                        break;
                    case 2:
                        executarCenario2();
                        break;
                    case 3:
                        executarCenario3();
                        break;
                    default:
                        System.out.println("Cenário inválido. Escolha 0, 1, 2 ou 3.");
                }
                aguardarTerminoThreads();
            }
        }
    }

    static void executarCenario1() {
        for (int i = 0; i < NUM_NODES; i++) {
            Thread thread = new Thread(threadGroup, new ComportamentoNo(i));
            thread.start();
        }
    }

    static void executarCenario2() {
        SemaforoBinario semaforo = new SemaforoBinario(1);

        for (int i = 0; i < NUM_NODES; i++) {
            Thread thread = new Thread(threadGroup, new ComportamentoSincronizado(i, semaforo));
            thread.start();
        }
    }

    static void executarCenario3() {
        SemaforoBinario semaforo = new SemaforoBinario(1);
        for (int i = 0; i < NUM_NODES; i++) {
            Thread thread = new Thread(threadGroup, new ComportamentoNoIncorreto(i, semaforo));
            thread.start();
        }
    }

    static void aguardarTerminoThreads() {
        while (threadGroup.activeCount() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class SemaforoBinario {
        private int valor;

        public SemaforoBinario(int initial) {
            valor = initial;
        }

        public synchronized void adquire() throws InterruptedException {
            while (valor <= 0) {
                wait();
            }
            valor--;
        }

        public synchronized void libera() {
            valor++;
            notify();
        }
    }

    static class ComportamentoNo implements Runnable {
        private final int idNo;

        public ComportamentoNo(int idNo) {
            this.idNo = idNo;
        }

        @Override
        public void run() {
            System.out.println("Nó " + idNo + " tentando acessar recurso compartilhado.");
            try {
                Thread.sleep(1000); // Simula algum processamento antes de acessar

                System.out.println("Nó " + idNo + " acessando o recurso compartilhado: " + RECURSO_COMPARTILHADO);
                Thread.sleep(2000); // Simula acesso ao recurso

                System.out.println("Nó " + idNo + " concluiu o acesso ao recurso compartilhado.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class ComportamentoNoIncorreto implements Runnable {
        private final int idNo;
        private final SemaforoBinario semaforo;

        public ComportamentoNoIncorreto(int idNo, SemaforoBinario semaforo) {
            this.idNo = idNo;
            this.semaforo = semaforo;
        }

        @Override
        public void run() {
            System.out.println("Nó " + idNo + " tentando acessar recurso compartilhado.");
            try {
                Thread.sleep(1000); // Simula algum processamento antes de acessar
                System.out.println("Nó " + idNo + " tentando adquirir o semáforo.");
                semaforo.adquire();
                System.out.println("Nó " + idNo + " adquiriu o semáforo. Acessando o recurso compartilhado: "
                        + RECURSO_COMPARTILHADO);
                Thread.sleep(2000); // Simula acesso ao recurso
                
                System.out.println("Nó " + idNo + " concluiu o acesso ao recurso compartilhado e não liberou o semáforo (INCORRETO).");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class ComportamentoSincronizado implements Runnable {
        private final int idNo;
        private final SemaforoBinario semaforo;

        public ComportamentoSincronizado(int idNo, SemaforoBinario semaforo) {
            this.idNo = idNo;
            this.semaforo = semaforo;
        }

        @Override
        public void run() {
            System.out.println("Nó " + idNo + " tentando acessar recurso compartilhado.");
            try {
                Thread.sleep(1000); // Simula algum processamento antes de acessar

                System.out.println("Nó " + idNo + " tentando adquirir o semáforo.");
                semaforo.adquire();
                System.out.println("Nó " + idNo + " adquiriu o semáforo. Acessando o recurso compartilhado: "
                        + RECURSO_COMPARTILHADO);
                Thread.sleep(2000); // Simula acesso ao recurso
                System.out.println("Nó " + idNo + " concluiu o acesso ao recurso compartilhado e liberou o semáforo.");
                semaforo.libera();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
