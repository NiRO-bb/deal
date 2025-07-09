package com.example.Deal;

import com.example.Deal.Controller.ContractorToRoleController;
import com.example.Deal.DTO.ContractorToRole;
import com.example.Deal.Service.ContractorToRoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ContractorToRoleControllerTest {

    private ContractorToRoleService service = Mockito.mock(ContractorToRoleService.class);
    private ContractorToRoleController controller = new ContractorToRoleController(service);

    @Test
    public void testSaveSuccess() {
        Mockito.when(service.add(any(ContractorToRole.Key.class))).thenReturn(new ContractorToRole());
        ResponseEntity<?> response = controller.add(new ContractorToRole.Key());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSaveFailure() {
        Mockito.when(service.add(any(ContractorToRole.Key.class))).thenThrow(new RuntimeException("error"));
        ResponseEntity<?> response = controller.add(new ContractorToRole.Key());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteSuccess() {
        Mockito.when(service.delete(any(ContractorToRole.Key.class))).thenReturn(new ContractorToRole());
        ResponseEntity<?> response = controller.delete(new ContractorToRole.Key());
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteNull() {
        Mockito.when(service.delete(any(ContractorToRole.Key.class))).thenReturn(null);
        ResponseEntity<?> response = controller.delete(new ContractorToRole.Key());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteFailure() {
        Mockito.when(service.delete(any(ContractorToRole.Key.class))).thenThrow(new RuntimeException("error"));
        ResponseEntity<?> response = controller.delete(new ContractorToRole.Key());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
