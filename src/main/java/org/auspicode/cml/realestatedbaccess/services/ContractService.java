package org.auspicode.cml.realestatedbaccess.services;

import jakarta.transaction.Transactional;
import org.auspicode.cml.realestatedbaccess.entities.ContractEntity;
import org.auspicode.cml.realestatedbaccess.entities.RoomEntity;
import org.auspicode.cml.realestatedbaccess.entities.UnitEntity;
import org.auspicode.cml.realestatedbaccess.mappers.ContractMapper;
import org.auspicode.cml.realestatedbaccess.models.ContractResponse;
import org.auspicode.cml.realestatedbaccess.models.CreateContractRequest;
import org.auspicode.cml.realestatedbaccess.repositories.ContractRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.auspicode.cml.realestatedbaccess.exception.ErrorMessages.CONTRACT_NOT_IN_DB;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    private final TenantService tenantService;

    private final LandlordService landlordService;

    private final UnitService unitService;

    private final RoomService roomService;

    private final ContractMapper contractMapper;

    public ContractService(ContractRepository contractRepository, TenantService tenantService, LandlordService landlordService, UnitService unitService, RoomService roomService, ContractMapper contractMapper) {
        this.contractRepository = contractRepository;
        this.tenantService = tenantService;
        this.landlordService = landlordService;
        this.unitService = unitService;
        this.roomService = roomService;
        this.contractMapper = contractMapper;
    }

    @Transactional
    public List<ContractResponse> retrieveContracts() {
        return contractMapper.toModel(contractRepository.findAll());
    }

    @Transactional
    public ContractResponse findOne(Long id) {
        ContractEntity contractEntity = contractRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(CONTRACT_NOT_IN_DB));
        return contractMapper.toModel(contractEntity);
    }

    @Transactional
    public List<ContractResponse> findByUnitId(String unitId) {
        UnitEntity unitEntity = unitService.findOneEntity(unitId);
        List<ContractEntity> contractEntityList = contractRepository.findByUnitId(unitEntity);
        if (contractEntityList.isEmpty()) {
            throw new NoSuchElementException(CONTRACT_NOT_IN_DB);
        }
        return contractMapper.toModel(contractEntityList);
    }

    /*public List<ContractResponse> findByTenantId(String nif) {
        TenantEntity tenantEntity = tenantService.findEntityByNif(nif);
        List<ContractEntity> contractEntityList = contractRepository.findByTenantId(tenantEntity);
        if (contractEntityList.isEmpty()) {
            throw new NoSuchElementException(CONTRACT_NOT_IN_DB);
        }
        return contractMapper.toModel(contractEntityList);
    }*/

    @Transactional
    public ContractResponse findByRoomId(Long id) {
        RoomEntity roomEntity = roomService.findOneEntity(id);
        Optional<ContractEntity> contractEntity = contractRepository.findByRoomId(roomEntity);
        if (contractEntity.isEmpty()) {
            throw new NoSuchElementException(CONTRACT_NOT_IN_DB);
        }
        return contractMapper.toModel(contractEntity.get());
    }

    @Transactional
    public ContractResponse createContract(CreateContractRequest contractRequest) {
        ContractEntity contractEntity = buildContractEntity(contractRequest);
        return contractMapper.toModel(contractRepository.save(contractEntity));
    }

    private ContractEntity buildContractEntity(CreateContractRequest contractRequest) {
        UnitEntity unitEntity = unitService.findOneEntity(contractRequest.getUnitId());
        RoomEntity roomEntity = roomService.findAvailableRoomToAddToContract(contractRequest.getRoomId(), unitEntity);
        ContractEntity contractEntity = contractMapper.createContractRequestToEntity(contractRequest, unitEntity, roomEntity);
        contractEntity.setTenants(tenantService.findTenantsByNif(contractRequest.getTenantsNif()));
        contractEntity.setLandlords(landlordService.findLandlordsByNif(contractRequest.getLandlordsNif()));
        return contractEntity;
    }

    @Transactional
    public ContractResponse deleteContract(Long id) {
        Optional<ContractEntity> contractEntity = contractRepository.findById(id);
        if (contractEntity.isEmpty()) {
            throw new NoSuchElementException(CONTRACT_NOT_IN_DB);
        }
        roomService.findRoomToDeleteFromContract(contractEntity.get().getRoomId());
        contractRepository.delete(contractEntity.get());
        return contractMapper.toModel(contractEntity.get());
    }
}
