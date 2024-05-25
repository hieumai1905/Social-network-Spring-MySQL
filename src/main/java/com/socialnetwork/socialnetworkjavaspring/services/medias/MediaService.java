package com.socialnetwork.socialnetworkjavaspring.services.medias;

import com.socialnetwork.socialnetworkjavaspring.models.Media;
import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.models.enums.MediaType;
import com.socialnetwork.socialnetworkjavaspring.repositories.IMediaRepository;
import com.socialnetwork.socialnetworkjavaspring.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Service
public class MediaService implements IMediaService{
    @Value("${root-path.dir}")
    private String CONTEXT_PATH_DIR;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Autowired
    private IMediaRepository mediaRepository;
    public List<String> saveList(List<MultipartFile> files, String postId, String userId) {
        List<String> medias = new ArrayList<>();
        if(files == null)
            return medias;

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                try {
                    File destFile = new File(CONTEXT_PATH_DIR + UPLOAD_DIR + File.separator + fileName);
                    destFile.getParentFile().mkdirs();
                    file.transferTo(destFile);

                    String fileExtension = getFileExtension(fileName);
                    MediaType mediaType;
                    if (isImage(fileExtension)) {
                        mediaType = MediaType.IMAGE;
                    } else if (isVideo(fileExtension)) {
                        mediaType = MediaType.VIDEO;
                    }else {
                        throw new Exception("Media Type is not allow");
                    }

                    Media media = new Media();
                    media.setUrl("/assets/files-upload" + Constants.FILE_SEPARATOR + fileName);
                    media.setPost(new Post());
                    media.getPost().setPostId(postId);
                    media.setType(mediaType);

                    mediaRepository.save(media);
                    medias.add(media.getUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return medias;
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(Constants.DOT)) {
            return fileName.substring(fileName.lastIndexOf(Constants.DOT) + Constants.NUMBER_ONE).toLowerCase();
        }
        return "";
    }

    private boolean isImage(String fileExtension) {
        return Arrays.asList(Constants.allowExtensionImages).contains(fileExtension);
    }

    private boolean isVideo(String fileExtension) {
        return  Arrays.asList(Constants.allowExtensionVideos).contains(fileExtension);
    }

    @Override
    public Optional<Media> save(Media object) {
        return Optional.empty();
    }

    @Override
    public Optional<Media> delete(Media object) {
        return Optional.empty();
    }
}
