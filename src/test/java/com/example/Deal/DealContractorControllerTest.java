package com.example.Deal;

import com.example.Deal.Controller.DealContractorController;
import com.example.Deal.DTO.DealContractor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.Deal.Service.DealContractorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class DealContractorControllerTest {

    private DealContractorService service = Mockito.mock(DealContractorService.class);
    private DealContractorController controller = new DealContractorController(service);

    @Test
    public void testSaveSuccess() {
        Mockito.when(service.save(any(DealContractor.class))).thenReturn(new DealContractor());
        ResponseEntity<?> response = controller.save(new DealContractor());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSaveNull() {
        Mockito.when(service.save(any(DealContractor.class))).thenReturn(null);
        ResponseEntity<?> response = controller.save(new DealContractor());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testSaveFailure() {
        Mockito.when(service.save(any(DealContractor.class))).thenThrow(new RuntimeException("error"));
        ResponseEntity<?> response = controller.save(new DealContractor());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteSuccess() {
        Mockito.when(service.delete(any(UUID.class))).thenReturn(new DealContractor());
        ResponseEntity<?> response = controller.delete(Mockito.mock(UUID.class));
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteNull() {
        Mockito.when(service.delete(any(UUID.class))).thenReturn(null);
        ResponseEntity<?> response = controller.delete(Mockito.mock(UUID.class));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteFailure() {
        Mockito.when(service.delete(any(UUID.class))).thenThrow(new RuntimeException("error"));
        ResponseEntity<?> response = controller.delete(Mockito.mock(UUID.class));
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
