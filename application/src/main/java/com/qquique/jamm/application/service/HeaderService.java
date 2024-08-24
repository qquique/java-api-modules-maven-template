package com.qquique.jamm.application.service;
import com.qquique.jamm.application.dto.HeaderDTO;
import com.qquique.jamm.application.dto.DetailDTO;
import com.qquique.jamm.application.mapper.DetailMapper;
import com.qquique.jamm.application.mapper.HeaderMapper;
import com.qquique.jamm.application.service.exception.ServiceException;
import com.qquique.jamm.domain.entity.Header;
import com.qquique.jamm.domain.entity.Detail;
import com.qquique.jamm.infrastructure.database.repository.HeaderRepositoryImpl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.qquique.jamm.infrastructure.database.exception.RepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import com.qquique.jamm.application.util.ErrorMessages;

public class HeaderService {

    private static final Logger logger = LogManager.getLogger(HeaderService.class);

    private final HeaderRepositoryImpl repository;

    public HeaderService(HeaderRepositoryImpl repository) {
        this.repository = repository;
    }

    public List<HeaderDTO> getAllHeaders() {
        try {
            List<Header> headerList = this.repository.findAll();
            List<HeaderDTO> headerDTOList = new ArrayList<>();
            headerList
                    .stream()
                    .map(HeaderMapper.INSTANCE::mapToDTO)
                    .forEach(headerDTOList::add);
            return headerDTOList;
        } catch (RepositoryException e) {
            String msg = ErrorMessages.getErrorMessage("service.error_retrieving_list");
            logger.error(msg, e);
            throw new ServiceException(msg, e);
        }
    }

    public HeaderDTO getHeaderById(Long id) {
        try {
            Optional<Header> headerOptional = this.repository.findById(id);
            if (headerOptional.isPresent()) {
                Header header = headerOptional.get();
                return HeaderMapper.INSTANCE.mapToDTO(header);
            }
        } catch (RepositoryException e) {
            String msg = ErrorMessages.getErrorMessage("service.error_retrieving_id", id);
            logger.error(msg, e);
            throw new ServiceException(msg, e);
        }
        return null;
    }

    public HeaderDTO createHeader(HeaderDTO headerDTO) {
        try {
            Header header = HeaderMapper.INSTANCE.mapToDomain(headerDTO);
            Header finalHeader = header;
            header.setDetails(headerDTO.getDetails()
                    .stream()
                    .map(detailDTO ->
                            DetailMapper.INSTANCE.mapToDomainWithHeader(detailDTO, finalHeader)
                    ).toList()
            );
            header = this.repository.save(header);
            return HeaderMapper.INSTANCE.mapToDTO(header);
        } catch (RepositoryException e) {
            String msg = ErrorMessages.getErrorMessage("service.error_creating_record");
            logger.error(msg, e);
            throw new ServiceException(msg, e);
        }
    }

    public HeaderDTO updateHeader(Long id, HeaderDTO headerDTO) {
        try {
            Optional<Header> headerOptional = this.repository.findById(id);
            if (headerOptional.isPresent()) {
                Header header = headerOptional.get();
                HeaderMapper.INSTANCE.updateHeaderFromDTO(headerDTO, header);
                List<Detail> existingDetails = header.getDetails();
                List<DetailDTO> updatedDetails = headerDTO.getDetails();

                // extract the new ones
                List<DetailDTO> newDetailsDTO = updatedDetails
                        .stream()
                        .filter(detailDTO -> detailDTO.getId() == null)
                        .toList();

                // remove the new ones, the null id's from the new ones produce errors in the next mapping
                updatedDetails.removeIf(detailsDTO -> detailsDTO.getId() == null);

                // map ids for search
                Map<Long, DetailDTO> updatedDetailMap = updatedDetails.stream()
                        .collect(Collectors.toMap(DetailDTO::getId, Function.identity()));

                Header finalHeader = header;

                // update domain from dto
                existingDetails.removeIf(detail -> {
                    DetailDTO updatedDetail = updatedDetailMap.get(detail.getId());
                    if (updatedDetail != null) {
                        DetailMapper.INSTANCE.updateDetailFromDTO(updatedDetail, detail, finalHeader);
                        return false;
                    } else {
                        return true;
                    }
                });

                // add the new ones to be inserted and id generated
                updatedDetails.addAll(newDetailsDTO);

                updatedDetails.stream()
                        .filter(detailDTO -> detailDTO.getId() == null)
                        .forEach(detailDTO -> {
                            Detail newDetail = DetailMapper.INSTANCE.mapToDomainWithHeader(detailDTO, finalHeader);
                            existingDetails.add(newDetail);
                        });

                header.setDetails(existingDetails);
                header = this.repository.save(header);
                return HeaderMapper.INSTANCE.mapToDTO(header);
            } else {
                return null;
            }
        } catch (IllegalStateException | RepositoryException e) {
            String msg = ErrorMessages.getErrorMessage("service.error_updating_id", id);
            logger.error(msg, e);
            throw new ServiceException(msg, e);
        }
    }

    public void deleteHeader(Long id) {
        try {
            this.repository.deleteById(id);
        } catch (RepositoryException e) {
            String msg = ErrorMessages.getErrorMessage("service.error_deleting_id", id);
            logger.error(msg, e);
            throw new ServiceException(msg, e);
        }
    }
}

