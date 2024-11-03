package br.com.willianserafim.gestao_vagas.exceptions;

public class JobClosedException  extends RuntimeException{
    public JobClosedException() {
        super("Você não pode aplicar para essa vaga porque ela foi encerrada.");
    }
}