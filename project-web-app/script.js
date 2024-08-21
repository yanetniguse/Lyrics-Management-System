document.addEventListener("DOMContentLoaded", function () {
  const loadingMessage = document.getElementById("loading-message");

  // Fetch lyrics data from the API endpoint
  fetchLyricsData();

  function fetchLyricsData() {
    fetch("http://localhost:8080/api/lyrics/get-all")
      .then((response) => response.json())
      .then((data) => {
        // Clear loading message
        loadingMessage.style.display = "none";
        handleLyricsData(data);
      })
      .catch((error) => {
        console.error("Error fetching lyrics:", error);
        loadingMessage.innerText =
          "Error fetching data. Please try again later";
      });
  }

  function handleLyricsData(data) {
    // Store the original data for sorting
    let originalData = [...data];

    // Render the table
    renderTable(data);

    // Add event listener to view lyrics button
    document.addEventListener("click", handleViewLyricsClick);

    // Selecting DOM elements
    const search = document.querySelector(".input-group input"),
      tableRows = document.querySelectorAll("tbody tr"),
      tableHeadings = document.querySelectorAll("thead th");

    // Searching for specific data of HTML table
    search.addEventListener("input", searchTable);

    // Sorting | Ordering data of HTML table
    let currentSortColumn = 0; // Initialize with ID column
    let sortAsc = true; // Initialize with ascending order

    tableHeadings.forEach((head, i) => {
      head.onclick = () => handleTableHeadingClick(i);
    });

    function handleViewLyricsClick(event) {
      const target = event.target;
      if (target.classList.contains("view-lyrics-btn")) {
        const title = target.getAttribute("data-title");
        const lyrics = target.getAttribute("data-lyrics");
        openDialog(title, lyrics);
      }
    }

    function searchTable() {
      const searchValue = search.value.trim().toLowerCase();
      const filteredData = originalData.filter((song) =>
        song.songTitle.toLowerCase().includes(searchValue)
      );
      renderTable(filteredData);
    }

    function handleTableHeadingClick(i) {
      // Toggle 'active' class for table headers
      tableHeadings.forEach((header) => header.classList.remove("active"));
      tableHeadings[i].classList.add("active");

      // Toggle 'active' class for corresponding table cells
      document
        .querySelectorAll("td")
        .forEach((td) => td.classList.remove("active"));
      tableRows.forEach((row) => {
        row.querySelectorAll("td")[i].classList.add("active");
      });

      if (currentSortColumn === i) {
        sortAsc = !sortAsc;
      } else {
        currentSortColumn = i;
        sortAsc = true;
      }

      sortTable(currentSortColumn, sortAsc);
    }

    function sortTable(column, asc) {
      const sortedData = [...originalData].sort((a, b) => {
        if (column === 0) {
          return asc ? a.id - b.id : b.id - a.id;
        } else if (column === 1 || column === 2) {
          const aValue =
            a[column === 1 ? "songTitle" : "songArtist"].toLowerCase();
          const bValue =
            b[column === 1 ? "songTitle" : "songArtist"].toLowerCase();
          return asc
            ? aValue.localeCompare(bValue)
            : bValue.localeCompare(aValue);
        }
      });

      renderTable(sortedData);

      // Update up arrow icon
      const upArrow = tableHeadings[column].querySelector(".icon-arrow");
      if (asc) {
        upArrow.innerHTML = "&uarr;";
      } else {
        upArrow.innerHTML = "&darr;";
      }
    }
  }

  function renderTable(data) {
    const tableBody = document.querySelector("tbody");
    tableBody.innerHTML = ""; // Clear existing table rows
    if (data.length === 0) {
      loadingMessage.innerText = "Song not found";
      loadingMessage.style.display = "block";
      return;
    }
    data.forEach((song) => {
      // Create table row for each song
      const newRow = document.createElement("tr");
      newRow.innerHTML = `
              <td>${song.id}</td>
              <td>${song.songTitle}</td>
              <td>${song.songArtist}</td>
              <td><button class="view-lyrics-btn" data-title="${song.songTitle}" data-lyrics="${song.songLyrics}">View Lyrics</button></td>
            `;

      // Append the new row to the table body
      tableBody.appendChild(newRow);
    });
    loadingMessage.style.display = "none";
  }

  // Function to open dialog with song title and lyrics
  function openDialog(title, lyrics) {
    const dialog = document.getElementById("dialog");
    dialog.querySelector("h2").innerText = title;
    dialog.querySelector("p").innerText = lyrics;
    dialog.showModal();
  }
});
