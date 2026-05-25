(function () {
    const K       = 3;
    let   offset  = 0;
    let   total   = 0;

    const tbody   = document.getElementById('photos-tbody');
    const btnPrev = document.getElementById('btn-prev');
    const btnNext = document.getElementById('btn-next');
    const info    = document.getElementById('photos-info');

    const isStaff = currentRole === 'staff'; // inject in dashboard

    function fetchPage(newOffset) {
        const xhr = new XMLHttpRequest();
        xhr.open('GET', 'api/photos_xml.php?offset=' + newOffset, true);
        xhr.onload = function () {
            if (xhr.status !== 200) {
                tbody.innerHTML = '<tr><td colspan="' + cols() + '">Error loading data.</td></tr>';
                return;
            }

            // response is xml, so no json parsing
            const xml    = xhr.responseXML;
            total        = parseInt(getText(xml, 'total')); // get total from the xml response
            offset       = parseInt(getText(xml, 'offset')); // same but wth offset
            const photos = xml.getElementsByTagName('photo');

            renderTable(photos);
            updateNav();
        };
        xhr.onerror = function () {
            tbody.innerHTML = '<tr><td colspan="' + cols() + '">Network error.</td></tr>';
        };
        xhr.send();
    }

    // get text content of a top-level tag
    function getText(xml, tag) {
        return xml.getElementsByTagName(tag)[0].textContent;
    }

    // get text of a tag inside a single <photo> element
    function getField(photo, tag) {
        return photo.getElementsByTagName(tag)[0].textContent;
    }

    function cols() {
        return isStaff ? 6 : 5;
    }

    function renderTable(photos) {
        if (photos.length === 0) {
            tbody.innerHTML = '<tr><td colspan="' + cols() + '">No photos found.</td></tr>';
            return;
        }

        let rows = '';
        for (let i = 0; i < photos.length; i++) {
            const r = photos[i];

            const id          = getField(r, 'id');
            const uploaderId  = parseInt(getField(r, 'uploader_id'));
            const filename    = getField(r, 'filename');
            const trailName   = getField(r, 'trail_name');
            const description = getField(r, 'description');
            const uploader    = getField(r, 'uploader');

            const uploaderCell = isStaff
                ? '<td>' + escHtml(uploader) + '</td>'
                : '';

            const canDelete = isStaff || (uploaderId === currentUserId);
            const deleteBtn = canDelete
                ? '<form method="POST" action="upload.php"'
                + ' onsubmit="return confirm(\'Delete this photo?\')">'
                + '<input type="hidden" name="action" value="delete">'
                + '<input type="hidden" name="photo_id" value="' + r.id + '">'
                + '<input type="hidden" name="csrf_token" value="' + csrfToken + '">'
                + '<button type="submit" class="btn">Delete</button>'
                + '</form>'
                : '—';

            rows += '<tr>'
                + '<td>' + (offset + i + 1) + '</td>'
                + '<td>' + escHtml(trailName)   + '</td>'
                + '<td>' + escHtml(description) + '</td>'
                + uploaderCell
                + '<td><img src="uploads/' + escHtml(filename) + '"'
                +      ' style="height:60px;object-fit:cover;" alt="photo"></td>'
                + '<td>' + deleteBtn + '</td>'
                + '</tr>';
        }

        tbody.innerHTML = rows;
    }

    function updateNav() {
        const page  = Math.floor(offset / K) + 1;
        const pages = Math.ceil(total / K) || 1;
        info.textContent = 'Page ' + page + ' of ' + pages + ' (' + total + ' total)';

        btnPrev.disabled = (offset === 0);
        btnNext.disabled = (offset + K >= total);
    }

    function escHtml(str) {
        const d = document.createElement('div');
        d.appendChild(document.createTextNode(str || ''));
        return d.innerHTML;
    }

    btnPrev.addEventListener('click', function () {
        if (offset - K >= 0) fetchPage(offset - K);
    });

    btnNext.addEventListener('click', function () {
        if (offset + K < total) fetchPage(offset + K);
    });

    fetchPage(0);
}());