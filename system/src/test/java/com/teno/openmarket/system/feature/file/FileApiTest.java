package com.teno.openmarket.system.feature.file;

import com.teno.openmarket.system.feature.SystemUserRoleControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(FileApi.class)
public class FileApiTest extends SystemUserRoleControllerTest {

    @MockitoBean
    private FileService fileService;

    @Test
    @DisplayName("이미지 파일을 업로드하면 200 OK와 업로드 결과를 반환해야 한다")
    void should_ReturnUrl_When_ValidImageUploaded() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test-image.png",
            "image/png",
            "dummy-content".getBytes()
        );

        String dir = "products";
        String expectedUrl = "https://cdn.openmarket.com/products/uuid.png";

        FileUploadResponse response = FileUploadResponse.builder()
                .url(expectedUrl)
                .originalFileName("test-image.png")
                .storedFileName("products/uuid.png")
                .build();

        given(fileService.uploadImage(any(), anyString()))
                .willReturn(response);

        // when & then
        mockMvc.perform(multipart("/api/v1/system/images")
                        .file(file)
                        .param("dir", dir)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data.url").value(expectedUrl))
                .andExpect(jsonPath("$.data.originalFileName").value("test-image.png"));
    }
}
