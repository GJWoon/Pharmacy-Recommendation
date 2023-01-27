package service

import com.dev.pharmacy.api.service.KakaoUriBuilderService
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

class KakaoAddressSearchServiceTest extends Specification {

    private KakaoUriBuilderService kakaoUriBuilderService;

    def setup(){
        kakaoUriBuilderService = new KakaoUriBuilderService();
    }

    def "buildUriByAddressSearch - 한글 파라미터일 경우 정상적으로 인코딩"(){
        given:
        String address = "서울 성북구"

        when:
        def uri = kakaoUriBuilderService.builderUriByAddressSearch(address)

        then:
        println uri;
    }
}
