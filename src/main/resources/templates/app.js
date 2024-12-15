document.getElementById('uploadForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];

    if (!file) {
        alert('Please select a file!');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    let homePage = document.getElementsByClassName('home-page');
    for(let i = 0 ; i < homePage.length ; i++){
        console.log(homePage[i])
        homePage[i].style.display = 'none';
    }
    let loading = document.getElementsByClassName('loading');
    for(let i = 0 ; i < loading.length ; i++){
        loading[i].removeAttribute('style');
    }
    try {
        const response = await fetch('http://localhost:8080/files', {
            method: 'POST',
            body: formData,
        });

        if (!response.ok) {
            throw new Error(`File upload failed with status: ${response.status}`);
        }

        const result = await response.json();
        console.log(result);

        for(let i = 0 ; i < loading.length ; i++){
            loading[i].style.display = 'none';
        }

        const tableBody = document.getElementById('dataTableBody');
        tableBody.innerHTML = '';  // Clear any existing rows

        result.forEach((item, index) => {
            const row = `
                <tr>
                    <td>${index + 1}</td>
                    <td>${item.username}</td>
                    <td>${item.easy}</td>
                    <td>${item.medium}</td>
                    <td>${item.hard}</td>
                    <td>${item.total}</td>
                    <td>${item.status}</td>
                </tr>
            `;
            tableBody.insertAdjacentHTML('beforeend', row);
        });

        // Initialize DataTables
        $('#dataTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                'copy', 'excel', 'pdf', 'csv'
            ],
            pageLength: 20,
            lengthMenu: [10, 20, 50, 100],
        });

        document.getElementById("dataTable").style.display = 'table';
        document.getElementById('result').textContent = 'File uploaded and data displayed successfully!';
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('result').textContent = 'Error: ' + error.message;
    }
});
