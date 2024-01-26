function deleteApi() {
    var leaveAbsenceId = [[${detail.id}]];;

    fetch('/admin/off/' + leaveAbsenceId, {
        method: 'DELETE'
    }).then(response => {
        if (response.ok) {
            alert("삭제가 완료되었습니다.");
            window.location.href = "/";
        } else {
            alert("삭제 중 문제가 발생했습니다.");
        }
    }).catch(error => {
        console.error('Error:', error);
    });
}