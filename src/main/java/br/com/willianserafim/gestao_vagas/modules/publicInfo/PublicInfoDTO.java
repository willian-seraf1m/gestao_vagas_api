package br.com.willianserafim.gestao_vagas.modules.publicInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublicInfoDTO {
    private long candidatesCount;
    private long companiesCount;
    private long jobsCount;
    private long jobsCreatedLastWeekCount;
}
