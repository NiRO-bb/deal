package com.example.Deal;

import com.example.Deal.Controller.DealController;
import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.DealGet;
import com.example.Deal.DTO.DealSearch;
import com.example.Deal.Service.DealService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class DealControllerTest {

    private DealService service = Mockito.mock(DealService.class);
    private DealController controller = new DealController(service);

    @Test
    public void testSaveSuccess() {
        Mockito.when(service.save(any(Deal.class))).thenReturn(new Deal());
        ResponseEntity<?> response = controller.save(new Deal());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSaveFailure() {
        Mockito.when(service.save(any(Deal.class))).thenThrow(new RuntimeException("error"));
        ResponseEntity<?> response = controller.save(new Deal());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testChangeSuccess() {
        Mockito.when(service.change(any(Deal.DealStatusUpdate.class))).thenReturn(new Deal());
        ResponseEntity<?> response = controller.change(new Deal.DealStatusUpdate());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testChangeNull() {
        Mockito.when(service.change(any(Deal.DealStatusUpdate.class))).thenReturn(null);
        ResponseEntity<?> response = controller.change(new Deal.DealStatusUpdate());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testChangeFailure() {
        Mockito.when(service.change(any(Deal.DealStatusUpdate.class))).thenThrow(new RuntimeException("error"));
        ResponseEntity<?> response = controller.change(new Deal.DealStatusUpdate());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetSuccess() {
        Mockito.when(service.get(any(UUID.class))).thenReturn(Optional.of(new DealGet()));
        ResponseEntity<?> response = controller.get(Mockito.mock(UUID.class));
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetFailure() {
        Mockito.when(service.get(any(UUID.class))).thenReturn(Optional.empty());
        ResponseEntity<?> response = controller.get(Mockito.mock(UUID.class));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testSearchSuccess() {
        Mockito.when(service.search(any(DealSearch.class), any(Integer.class), any(Integer.class)))
                .thenReturn(Collections.singletonList(new DealGet()));
        ResponseEntity<?> response = controller.search(new DealSearch(), 0, 0);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSearchFailure() {
        Mockito.when(service.search(any(DealSearch.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new ArrayList<>());
        ResponseEntity<?> response = controller.search(new DealSearch(), 0, 0);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
