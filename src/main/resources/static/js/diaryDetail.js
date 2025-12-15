// 로그인 체크
const user = JSON.parse(localStorage.getItem('user'));
if (!user) {
    alert('로그인이 필요합니다.');
    location.href = '/login.html';
}

// URL에서 일기 ID 가져오기
const urlParams = new URLSearchParams(window.location.search);
const diaryId = urlParams.get('id');

if (!diaryId) {
    alert('잘못된 접근입니다.');
    location.href = '/';
}

let currentDiary = null;
let selectedEmotion = '';

// 감정 선택
const emotionButtons = document.querySelectorAll('.emotion-btn');
emotionButtons.forEach(btn => {
    btn.addEventListener('click', () => {
        // 모든 버튼에서 active 제거
        emotionButtons.forEach(b => b.classList.remove('active'));
        // 클릭한 버튼에 active 추가
        btn.classList.add('active');
        selectedEmotion = btn.dataset.emotion;
    });
});

// 일기 상세 정보 로드
async function loadDiary() {
    try {
        const response = await fetch(`/api/diaries/${diaryId}`);

        if (!response.ok) {
            alert('일기를 찾을 수 없습니다.');
            location.href = '/';
            return;
        }

        currentDiary = await response.json();

        // 날짜 표시
        const date = new Date(currentDiary.createdAt);
        const dateDisplay = date.toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            weekday: 'long'
        });
        document.getElementById('dateDisplay').textContent = dateDisplay;

        // 내용 채우기
        document.getElementById('content').value = currentDiary.content;

        // 감정 버튼 활성화
        selectedEmotion = currentDiary.emotion;
        emotionButtons.forEach(btn => {
            if (btn.dataset.emotion === currentDiary.emotion) {
                btn.classList.add('active');
            }
        });

    } catch (error) {
        console.error('일기 로드 실패:', error);
        alert('일기를 불러오는데 실패했습니다.');
        location.href = '/';
    }
}

// 일기 수정 폼 제출
document.getElementById('diaryEditForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const content = document.getElementById('content').value;
    const errorMessage = document.getElementById('errorMessage');

    if (content.trim().length === 0) {
        errorMessage.textContent = '일기 내용을 입력해주세요.';
        errorMessage.style.display = 'block';
        return;
    }

    if (content.trim().length < 5) {
        errorMessage.textContent = '최소 5자 이상 입력해주세요.';
        errorMessage.style.display = 'block';
        return;
    }

    try {
        const response = await fetch(`/api/diaries/${diaryId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                title: content.substring(0, 20), // 내용 앞 20자를 제목으로
                content: content.trim(),
                emotion: selectedEmotion
            })
        });

        const result = await response.text();

        if (response.ok) {
            alert('일기가 수정되었습니다!');
            location.href = '/';
        } else {
            errorMessage.textContent = result;
            errorMessage.style.display = 'block';
        }
    } catch (error) {
        console.error('일기 수정 실패:', error);
        errorMessage.textContent = '일기 수정 중 오류가 발생했습니다.';
        errorMessage.style.display = 'block';
    }
});

// 일기 삭제
async function deleteDiary() {
    if (!confirm('정말로 이 일기를 삭제하시겠습니까?')) {
        return;
    }

    try {
        const response = await fetch(`/api/diaries/${diaryId}`, {
            method: 'DELETE'
        });

        const result = await response.text();

        if (response.ok) {
            alert('일기가 삭제되었습니다.');
            location.href = '/';
        } else {
            alert(result);
        }
    } catch (error) {
        console.error('일기 삭제 실패:', error);
        alert('일기 삭제 중 오류가 발생했습니다.');
    }
}

// 페이지 로드 시 일기 정보 로드
loadDiary();
