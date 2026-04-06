document.addEventListener("DOMContentLoaded", function(){
	const ageInput = document.getElementById("age");

	const rescue = document.querySelector('input[value=rescue]');
	const fireRanger = document.querySelector('input[value="fire_ranger"]');

	ageInput.addEventListener("input", function () {
		const age = parseInt(ageInput.value);

		if(isNaN(age)) return;

		if(age < 25){
			rescue.disabled = true;
			fireRanger.disabled = true;

			rescue.checked = false;
			fireRanger.checked = false;
		} else {
			rescue.disabled = false;
			fireRanger.disabled = false;
		}
	});
});

document.addEventListener("DOMContentLoaded", function (){
	const zoneSelect = document.getElementById("zone");
	const parkSelect = document.getElementById("park");

	fetch("parks.json").then(res => res.json())
	.then(data => {
		for(let zone in data){
			const option = document.createElement("option");
			option.value = zone;
			option.textContent = zone;
			zoneSelect.appendChild(option);
		}

		zoneSelect.addEventListener("change", function() {
			const selectedZone = zoneSelect.value;

			parkSelect.innerHTML = '<option>Select park</option>';

			if(!selectedZone) return ;

			data[selectedZone].forEach(park => {
				const option = document.createElement("Option");

				option.value = park;
				option.textContent = park;
				parkSelect.appendChild(option);
			});
		});
	});
});

