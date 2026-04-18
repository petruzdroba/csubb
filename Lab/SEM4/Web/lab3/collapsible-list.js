document.addEventListener("DOMContentLoaded", () => {
  const items = document.querySelectorAll("li");

  items.forEach((li) => {
    const hasSublist = li.querySelector(":scope > ul, :scope > ol");

    if (!hasSublist) return;

    li.classList.add("has-sublist");

    li.addEventListener("click", (e) => {
      // checkbox click no toggle
      if (e.target.tagName === "INPUT") return;

      // inside click no toggle
      if (!li.contains(e.target)) return;

      li.classList.toggle("open");
    });
  });
});