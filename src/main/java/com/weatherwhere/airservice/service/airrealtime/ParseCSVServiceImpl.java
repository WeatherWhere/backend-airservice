package com.weatherwhere.airservice.service.airrealtime;


import com.weatherwhere.airservice.dto.AddrDto;

import com.weatherwhere.airservice.dto.airrealtime.StationNameDto;
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
    @Override
    public List<StationNameDto> ParseCSV() {
        ClassPathResource resource = new ClassPathResource("station_list.csv");
        List<StationNameDto> stationNames = new ArrayList<>();
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

                StationNameDto stationNameDto = StationNameDto.builder()
                        .stationName(data[1])
                        .build();

                stationNames.add(stationNameDto);
            }


        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return stationNames;
    }


    @Override
    public List<AddrDto> addrParseCSV() {
        ClassPathResource resource = new ClassPathResource("change_addr.csv");
        List<AddrDto> addrList = new ArrayList<>();
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
                AddrDto addrDto = AddrDto.builder()
                        .city(data[0])
                        .regionName(data[1])
                        .build();

                addrList.add(addrDto);
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return addrList;
    }

}

