//package com.usg.book.adapter.out.api;
//
//import com.google.cloud.storage.Blob;
//import com.google.cloud.storage.BlobInfo;
//import com.google.cloud.storage.Storage;
//import com.usg.book.domain.Image;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.io.InputStream;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class ImageGcsAdapterTest {
//
//    @InjectMocks
//    private ImageGcsAdapter imageGcsAdapter;
//    @Mock
//    private Storage storage;
//
//    @BeforeEach
//    public void setUp() {
//        imageGcsAdapter = new ImageGcsAdapter("bucketName", "projectId", storage);
//    }
//
//    @Test
//    @DisplayName("이미지 업로드 테스트 (GCS 연동 X)")
//    void uploadImageTest() {
//        // given
//        MockMultipartFile imageFile = new MockMultipartFile("images", "image1.jpg", "image/jpeg", new byte[10]);
//        Image image = Image
//                .builder()
//                .image(imageFile)
//                .bookId(1L)
//                .gcsUrl("gcsUrl")
//                .storeFilename("storeFilename")
//                .build();
//        Blob mockBlob = mock(Blob.class);
//
//        // stub
//        when(storage.create(any(BlobInfo.class), any(InputStream.class))).thenReturn(mockBlob);
//
//        // when
//        String url = imageGcsAdapter.uploadImage(image);
//
//        // then
//        assertThat(url).isEqualTo("https://storage.googleapis.com/" + "bucketName" + "/" + image.getStoreFilename());
//    }
//}