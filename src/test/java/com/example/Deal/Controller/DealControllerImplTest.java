package com.example.Deal.Controller;

import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.request.ChangeStatus;
import com.example.Deal.DTO.response.DealGet;
import com.example.Deal.DTO.request.DealSearch;
import com.example.Deal.Service.DealService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class DealControllerImplTest {

    private DealService service = Mockito.mock(DealService.class);
    private DealController controller = new DealControllerImpl(service);

    @Test
    public void testSaveSuccess() {
        Mockito.when(service.save(any(Deal.class))).thenReturn(new Deal());
        ResponseEntity<?> response = controller.save(new Deal());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSaveFailure() {
        Mockito.when(service.save(any(Deal.class))).thenThrow(new RuntimeException("error"));
        Assertions.assertThrows(RuntimeException.class, () -> controller.save(new Deal()));
    }

    @Test
    public void testChangeSuccess() {
        Mockito.when(service.change(any(ChangeStatus.class))).thenReturn(Optional.of(new Deal()));
        ResponseEntity<?> response = controller.change(new ChangeStatus());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testChangeEmpty() {
        Mockito.when(service.change(any(ChangeStatus.class))).thenReturn(Optional.empty());
        ResponseEntity<?> response = controller.change(new ChangeStatus());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testChangeFailure() {
        Mockito.when(service.change(any(ChangeStatus.class))).thenThrow(new RuntimeException("error"));
        Assertions.assertThrows(RuntimeException.class, () -> controller.change(new ChangeStatus()));
    }

    @Test
    public void testGetSuccess() {
        Mockito.when(service.get(any(UUID.class))).thenReturn(Optional.of(new DealGet()));
        ResponseEntity<?> response = controller.get(Mockito.mock(UUID.class));
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetEmpty() {
        Mockito.when(service.get(any(UUID.class))).thenReturn(Optional.empty());
        ResponseEntity<?> response = controller.get(Mockito.mock(UUID.class));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetFailure() {
        Mockito.when(service.get(any(UUID.class))).thenThrow(new RuntimeException("error"));
        Assertions.assertThrows(RuntimeException.class, () -> controller.get(Mockito.mock(UUID.class)));
    }

    @Test
    public void testSearchSuccess() {
        Mockito.when(service.search(any(DealSearch.class), any(Integer.class), any(Integer.class)))
                .thenReturn(Collections.singletonList(new DealGet()));
        ResponseEntity<?> response = controller.search(new DealSearch(), 0, 0);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSearchEmpty() {
        Mockito.when(service.search(any(DealSearch.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new ArrayList<>());
        ResponseEntity<?> response = controller.search(new DealSearch(), 0, 0);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testSearchFailure() {
        Mockito.when(service.search(any(DealSearch.class), any(Integer.class), any(Integer.class)))
                .thenThrow(new RuntimeException("error"));
        Assertions.assertThrows(RuntimeException.class, () -> controller.search(new DealSearch(), 0, 0));
    }

    @Test
    public void testExportSuccess() {
        Mockito.when(service.export(any(DealSearch.class))).thenReturn(Optional.of(Mockito.mock(InputStreamResource.class)));
        ResponseEntity<?> response = controller.export(new DealSearch());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testExportEmpty() {
        Mockito.when(service.export(any(DealSearch.class))).thenReturn(Optional.empty());
        ResponseEntity<?> response = controller.export(new DealSearch());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testExportFailure() {
        Mockito.when(service.export(any(DealSearch.class))).thenThrow(new RuntimeException("error"));
        Assertions.assertThrows(RuntimeException.class, () -> controller.export(new DealSearch()));
    }

}
