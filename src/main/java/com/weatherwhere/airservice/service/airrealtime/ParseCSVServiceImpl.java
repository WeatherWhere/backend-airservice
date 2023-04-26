package com.weatherwhere.airservice.service.airrealtime;


import com.weatherwhere.airservice.dto.airforecast.AddrDTO;

import com.weatherwhere.airservice.dto.airrealtime.StationNameDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ParseCSVServiceImpl implements ParseCSVService {
    /**
     * CSV 파일을 읽어 StationNameDTO 객체를 생성하고, 이를 담고 있는 List(List<StationNameDTO>)를 리턴합니다.
     *
     * @return 측정소 명 List(List<StationNameDTO>)를 리턴
     */
    @Override
    public List<StationNameDTO> ParseCSV() {
        ClassPathResource resource = new ClassPathResource("station_list.csv");
        List<StationNameDTO> stationNames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            boolean flag = false;

            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }

                // 첫 줄을 읽지 않음
                if (flag == false) {
                    flag = true;
                    continue;
                }

                String[] data = line.split(",");

                StationNameDTO stationNameDTO = StationNameDTO.builder()
                        .stationName(data[1])
                        .build();

                stationNames.add(stationNameDTO);
            }


        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return stationNames;
    }

    /**
     * CSV 파일을 읽어 AddrDTO 객체를 생성하고, 이를 담고 있는 List(List<AddrDTO>)를 리턴합니다.
     *
     * @return 주소 List(List<AddrDTO>)를 리턴
     */
    @Override
    public List<AddrDTO> addrParseCSV() {
        ClassPathResource resource = new ClassPathResource("change_addr.csv");
        List<AddrDTO> addrList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            boolean flag = false;

            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }

                // 첫 줄을 읽지 않음
                if (flag == false) {
                    flag = true;
                    continue;
                }

                String[] data = line.split(",");
                AddrDTO addrDTO = AddrDTO.builder()
                        .city(data[0])
                        .regionName(data[1])
                        .build();

                addrList.add(addrDTO);
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return addrList;
    }

}

