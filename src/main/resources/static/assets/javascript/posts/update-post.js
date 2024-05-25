function findPostById(postId){
    $.ajax({
        url: '/api/posts/' + postId,
        type: 'GET',
        success: function(response) {
            console.log('Find post with id: ', response);
            if(response.code === 200) {
                $("#content").val(response.data.postContent);
                $("#access").val(response.data.access);
                $("#post-id").val(response.data.postId);
                resetUsersTag();
                for (let userTag of response.data.userTags)
                    addUserTag(userTag);
                displayMediaToUI(response);
            }
        },
        error: function(xhr, status, error) {
            console.error('Error finding post:', error);
        }
    });
}

function createBlobFromUrl(url) {
    return fetch(url)
        .then(response => response.blob())
        .then(blob => blob)
        .catch(error => {
            console.error('Error creating Blob from URL:', error);
            throw error;
        });
}

function displayMediaToUI(response) {
    $("#file-list").empty();
    files = [];
    let postModal = $("#createPostModal");
    postModal.find(".modal-title").text('Update post');
    postModal.modal('show');
    const medias = response.data.medias;
    const filePromises = medias.map(media => {
        return createBlobFromUrl(media)
            .then(blob => {
                const fileName = extractFileNameFromUrl(media);
                return blobToFile(blob, fileName);
            })
            .catch(error => {
                console.error('Error creating file from URL:', error);
                throw error;
            });
    });

    Promise.all(filePromises)
        .then(files => {
            handleFiles(files);
        })
        .catch(error => {
            console.error('Error processing files:', error);
        });
}

function extractFileNameFromUrl(url) {
    return url.substring(url.lastIndexOf('/') + 1);
}

function blobToFile(blob, fileName) {
    return new File([blob], fileName, {
        type: blob.type,
        lastModified: Date.now()
    });
}
