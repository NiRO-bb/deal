package com.example.Deal.Controller;

import com.example.Deal.DTO.ContractorToRole;
import com.example.Deal.Service.ContractorToRoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ContractorToRoleControllerTest {

    private ContractorToRoleService service = Mockito.mock(ContractorToRoleService.class);
    private ContractorToRoleController controller = new ContractorToRoleController(service);

    @Test
    public void testSaveSuccess() {
        Mockito.when(service.add(any(ContractorToRole.Key.class))).thenReturn(Optional.of(new ContractorToRole()));
        ResponseEntity<?> response = controller.add(new ContractorToRole.Key());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSaveFailure() {
        Mockito.when(service.add(any(ContractorToRole.Key.class))).thenThrow(new RuntimeException("error"));
        Assertions.assertThrows(RuntimeException.class, () -> controller.add(new ContractorToRole.Key()));
    }

    @Test
    public void testDeleteSuccess() {
        Mockito.when(service.delete(any(ContractorToRole.Key.class))).thenReturn(Optional.of(new ContractorToRole()));
        ResponseEntity<?> response = controller.delete(new ContractorToRole.Key());
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteEmpty() {
        Mockito.when(service.delete(any(ContractorToRole.Key.class))).thenReturn(Optional.empty());
        ResponseEntity<?> response = controller.delete(new ContractorToRole.Key());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteFailure() {
        Mockito.when(service.delete(any(ContractorToRole.Key.class))).thenThrow(new RuntimeException("error"));
        Assertions.assertThrows(RuntimeException.class, () -> controller.delete(new ContractorToRole.Key()));
    }

}
