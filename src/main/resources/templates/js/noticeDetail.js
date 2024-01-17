// noticeDetail.js
document.addEventListener('DOMContentLoaded', function () {
    // Simulated data
    const noticeData = {
        id: 1,
        title: '공지 1',
        contents: '이곳에 공지사항 내용이 들어갑니다.\n\n\n\n\n\n\n\n 감사합니다 ',
    };

    const noticeTitle = document.getElementById('noticeTitle');
    const noticeContents = document.getElementById('noticeContents');

    // Set title and contents
    noticeTitle.textContent = noticeData.title;
    noticeContents.textContent = noticeData.contents;
});
