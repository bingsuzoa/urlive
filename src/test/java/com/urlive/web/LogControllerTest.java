//package com.urlive.web;
//
//
//import com.urlive.domain.infrastructure.log.LogService;
//import com.urlive.global.responseFormat.ApiResponse;
//import com.urlive.global.responseFormat.ApiResponseBuilder;
//import com.urlive.service.UrliveFacade;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(LogController.class)
//@ActiveProfiles("test")
//@AutoConfigureMockMvc(addFilters = false)
//public class LogControllerTest {
//
//    @MockBean
//    UrliveFacade urliveFacade;
//
//    @MockBean
//    LogService logService;
//
//    @MockBean
//    ApiResponseBuilder apiResponseBuilder;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    @DisplayName("리다이렉트 확인")
//    void 리다이렉트_확인() throws Exception {
//        when(urliveFacade.decodeShortUrl(any())).thenReturn("http://test.com");
//
//        mockMvc.perform(get("/{short-url}", "shortUrl"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(header().string("Location", "http://test.com"));
//    }
//
//    @Test
//    @DisplayName("날짜별 유입량 확인")
//    void 유입량_확인() throws Exception {
//        List<Map<String, Object>> result = new ArrayList<>();
//        when(logService.getTrafficsByDateRange(anyInt(), any(), any(), any())).thenReturn(result);
//
//        when(apiResponseBuilder.ok(any(), any()))
//                .thenReturn(
//                        ResponseEntity.status(HttpStatus.OK)
//                                .body(new ApiResponse<>(200, "message", null)));
//
//        mockMvc.perform(get("/user-urls/{short-url}/date", "shortUrl")
//                .param("days", "7"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("날짜별 유입 경로별 유입량 확인")
//    void 날짜별_유입경로별_유입량_확인() throws Exception {
//        List<Map<String, Object>> result = new ArrayList<>();
//        when(logService.getTrafficsByReferer(anyInt(), any(), any(), any())).thenReturn(result);
//
//        when(apiResponseBuilder.ok(any(), any()))
//                .thenReturn(
//                        ResponseEntity.status(HttpStatus.OK)
//                                .body(new ApiResponse<>(200, "message", null)));
//
//        mockMvc.perform(get("/user-urls/{short-url}/referer", "shortUrl")
//                        .param("days", "7"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("날짜별 디바이스별 유입량 확인")
//    void 날짜별_디바이스별_유입량_확인() throws Exception {
//        List<Map<String, Object>> result = new ArrayList<>();
//        when(logService.getTrafficsByDevice(anyInt(), any(), any(), any())).thenReturn(result);
//
//        when(apiResponseBuilder.ok(any(), any()))
//                .thenReturn(
//                        ResponseEntity.status(HttpStatus.OK)
//                                .body(new ApiResponse<>(200, "message", null)));
//
//        mockMvc.perform(get("/user-urls/{short-url}/device", "shortUrl")
//                        .param("days", "7"))
//                .andExpect(status().isOk());
//    }
//}
