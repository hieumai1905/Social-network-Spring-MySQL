package com.socialnetwork.socialnetworkjavaspring.services.medias;

import com.socialnetwork.socialnetworkjavaspring.models.Media;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMediaService extends IGeneralService<Media, Long> {
    List<String> saveList(List<MultipartFile> files, String postId, String userId);
}
