// noticeList.js
document.addEventListener('DOMContentLoaded', function () {
    // Simulated data
    const noticeList = [
        { id: 1, title: '공지 1' },
        { id: 2, title: '공지 2' },
        { id: 3, title: '공지 3' },
    ];

    const noticeListContainer = document.getElementById('noticeList');

    // Populate notice list
    noticeList.forEach(notice => {
        const noticeBox = document.createElement('li');
        noticeBox.className = 'notice-box';
        noticeBox.dataset.noticeId = notice.id;
        noticeBox.innerHTML = `<h2>${notice.title}</h2>`;
        noticeListContainer.appendChild(noticeBox);
    });

    // Add event listener for notice item click
    noticeListContainer.addEventListener('click', function (event) {
        const clickedItem = event.target.closest('.notice-box');
        if (clickedItem) {
            const noticeId = clickedItem.dataset.noticeId;
            // Redirect to notice_detail.html with noticeId
            window.location.href = `notice_detail.html?id=${noticeId}`;
        }
    });
});
