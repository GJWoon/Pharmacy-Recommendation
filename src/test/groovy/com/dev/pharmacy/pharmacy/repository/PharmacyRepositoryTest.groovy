package com.dev.pharmacy.pharmacy.repository

import com.dev.pharmacy.pharmacy.entity.Pharmacy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class PharmacyRepositoryTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    def "PharmacyRepositorySave"(){

        given:
        String address = "서울 특별시 성북구 종암동"
        String name = "은혜 약국"
        double lat = 36.11
        double longitude = 128.11

        def pharmacy = Pharmacy.
                builder()
        .pharmacyAddress(address)
        .latitude(lat)
        .longitude(longitude)
        .pharmacyName(name)
        .build()


        when:
        def result = pharmacyRepository.save(pharmacy)

        then:
        result.getPharmacyAddress() == address

        }

}
