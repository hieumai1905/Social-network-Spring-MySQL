let dropZone = document.getElementById('drop-zone');
let fileInput = document.getElementById('file-input');
let fileList = document.getElementById('file-list');
let files = [];
dropZone.addEventListener('dragover', (e) => {
    e.preventDefault();
    dropZone.classList.add('drag-over');
});

dropZone.addEventListener('dragleave', () => {
    dropZone.classList.remove('drag-over');
});

dropZone.addEventListener('drop', (e) => {
    e.preventDefault();
    dropZone.classList.remove('drag-over');
    handleFiles(e.dataTransfer.files);
});

dropZone.addEventListener('click', () => {
    fileInput.click();
});

fileInput.addEventListener('change', () => {
    handleFiles(fileInput.files);
});

function handleFiles(filesArray) {
    let index = 0;
    for (const file of filesArray) {
        files.push(file);
        console.log(files);
        let li = document.createElement('li');
        let fileType = file.type.split('/')[0];
        let mediaContainer = document.createElement('div');
        mediaContainer.style.position = 'relative';
        mediaContainer.style.height = '200px';
        mediaContainer.style.width = '200px';

        if (fileType === 'image') {
            const img = document.createElement('img');
            img.src = URL.createObjectURL(file);
            img.style.marginBottom = '5px';
            img.style.marginRight = '5px';
            img.classList.add('create-post');
            img.height = '100px';
            mediaContainer.appendChild(img);
        } else if (fileType === 'video') {
            const video = document.createElement('video');
            video.src = URL.createObjectURL(file);
            video.controls = true;
            video.style.marginBottom = '5px';
            video.style.marginRight = '5px';
            video.classList.add('create-post');
            video.height = '100px';
            mediaContainer.appendChild(video);
        } else if (fileType === 'audio') {
            const audio = document.createElement('audio');
            audio.src = URL.createObjectURL(file);
            audio.controls = true;
            audio.style.marginBottom = '5px';
            audio.style.marginRight = '5px';
            audio.classList.add('create-post');
            audio.height = '100px';
            mediaContainer.appendChild(audio);
        }

        const deleteBtn = document.createElement('button');
        deleteBtn.textContent = 'x';
        deleteBtn.style.backgroundColor = 'gray';
        deleteBtn.classList.add('delete-btn');
        deleteBtn.addEventListener('click', () => {
            li.remove();
            const index = files.indexOf(file);
            if (index > -1) {
                files.splice(index, 1);
            }
        });

        mediaContainer.appendChild(deleteBtn);

        li.appendChild(mediaContainer);
        fileList.appendChild(li);
        index++;
    }
}
