package br.com.willianserafim.gestao_vagas.exceptions;

public class ApplicationJobFoundExeption extends RuntimeException{
    public ApplicationJobFoundExeption() {
        super("Você já aplicou para essa vaga.");
    }
}
