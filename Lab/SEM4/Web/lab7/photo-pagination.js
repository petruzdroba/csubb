(function () {
    const K       = 3;
    let   offset  = 0;
    let   total   = 0;

    const tbody   = document.getElementById('photos-tbody');
    const btnPrev = document.getElementById('btn-prev');
    const btnNext = document.getElementById('btn-next');
    const info    = document.getElementById('photos-info');

    const isStaff = currentRole === 'staff'; // injected by dashboard.php

    function fetchPage(newOffset) {
        const xhr = new XMLHttpRequest();
        xhr.open('GET', 'api/photos.php?offset=' + newOffset, true);
        xhr.onload = function () {
            if (xhr.status !== 200) {
                // if not OK, show err
                tbody.innerHTML = '<tr><td colspan="' + cols() + '">Error loading data.</td></tr>';
                return;
            }
            const data = JSON.parse(xhr.responseText);
            total  = data.total;
            offset = data.offset;
            renderTable(data.records);
            updateNav();
        };
        xhr.onerror = function () {
            tbody.innerHTML = '<tr><td colspan="' + cols() + '">Network error.</td></tr>';
        };
        xhr.send();
    }

    function cols() {
        // staff has uploader column, regular user does not
        return isStaff ? 6 : 5;
    }

    function renderTable(records) {
        if (records.length === 0) {
            tbody.innerHTML = '<tr><td colspan="' + cols() + '">No photos found.</td></tr>';
            return;
        }

        tbody.innerHTML = records.map(function (r, i) { 
            // based on role we change what each user sees
            const uploaderCell = isStaff
                ? '<td>' + escHtml(r.uploader) + '</td>'
                : ''; // only staff can see who uploaded photo

            const canDelete = isStaff || (r.uploader_id === currentUserId);
            const deleteBtn = canDelete
                ? '<form method="POST" action="upload.php"'
                + ' onsubmit="return confirm(\'Delete this photo?\')">'
                + '<input type="hidden" name="action" value="delete">'
                + '<input type="hidden" name="photo_id" value="' + r.id + '">'
                // send a token for delete endpoint
                + '<input type="hidden" name="csrf_token" value="' + csrfToken + '">'
                + '<button type="submit" class="btn">Delete</button>'
                + '</form>'
                : '—';//disable button if not staff
                
            return '<tr>'
                // global idx of photo
                + '<td>' + (offset + i + 1) + '</td>' 
                + '<td>' + escHtml(r.trail_name) + '</td>'
                + '<td>' + escHtml(r.description) + '</td>'
                + uploaderCell
                + '<td><img src="uploads/' + escHtml(r.filename) + '"'
                +      ' style="height:60px;object-fit:cover;" alt="photo"></td>'
                + '<td>' + deleteBtn + '</td>'
                + '</tr>';
        }).join('');
    }

    function updateNav() {
        // update the nav at the bottom to reflect the nr of pages
        const page  = Math.floor(offset / K) + 1;
        const pages = Math.ceil(total / K) || 1;
        info.textContent = 'Page ' + page + ' of ' + pages + ' (' + total + ' total)';

        // update buttons to disable on first and last
        btnPrev.disabled = (offset === 0); 
        btnNext.disabled = (offset + K >= total);
    }

    function escHtml(str) {
        const d = document.createElement('div');
        d.appendChild(document.createTextNode(str || ''));
        return d.innerHTML;
    }

    btnPrev.addEventListener('click', function () {
        if (offset - K >= 0) // dont go into negative pages/offsets
            fetchPage(offset - K); 
    });

    btnNext.addEventListener('click', function () {
        if (offset + K < total) // dont go paost the last page
            fetchPage(offset + K);
    });

    fetchPage(0); // load first page on init
}());