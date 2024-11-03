package br.com.willianserafim.gestao_vagas.modules.publicInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public-info")
public class PublicInfoController {
    @Autowired
    PublicInfoUseCase publicInfoUseCase;

    @GetMapping
    public PublicInfoDTO getPublicInfo() {
        return this.publicInfoUseCase.getPublicInfo();
    }
}
