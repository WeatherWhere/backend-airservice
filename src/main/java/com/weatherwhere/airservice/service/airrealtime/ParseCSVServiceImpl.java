package com.weatherwhere.airservice.service.airrealtime;


import com.weatherwhere.airservice.dto.AddrDTO;

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

