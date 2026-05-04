function setInvalid(input) {
  $(input).addClass("invalid");
}

function clearInvalid(input) {
  $(input).removeClass("invalid");
}

function isEmpty(value) {
  return value.trim() === "";
}

function isValidEmail(value) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
}

function isValidPhone(value) {
  return /^[0-9\s\-\+\(\)]+$/.test(value.trim());
}

const validators = {

  text(input) {
    if (isEmpty(input.value)) {
      setInvalid(input);
      return false;
    }
    clearInvalid(input);
    return true;
  },

  email(input) {
    if (isEmpty(input.value)) {
      setInvalid(input);
      return false;
    }
    if (!isValidEmail(input.value)) {
      setInvalid(input);
      return false;
    }
    clearInvalid(input);
    return true;
  },

  tel(input) {
    if (isEmpty(input.value)) {
      setInvalid(input);
      return false;
    }
    if (!isValidPhone(input.value)) {
      setInvalid(input);
      return false;
    }
    clearInvalid(input);
    return true;
  },

  number(input) {
    const raw = input.value.trim();
    if (isEmpty(raw)) {
      setInvalid(input);
      return false;
    }
    const value = Number(raw);
    const min = input.min !== "" ? Number(input.min) : -Infinity;
    const max = input.max !== "" ? Number(input.max) : Infinity;
    if (isNaN(value) || value < min || value > max) {
      setInvalid(input);
      return false;
    }
    clearInvalid(input);
    return true;
  },

  date(input) {
    if (isEmpty(input.value)) {
      setInvalid(input);
      return false;
    }
    const today = new Date().toISOString().split("T")[0]; // exp 2026-04-17
    if (input.value > today) {
      setInvalid(input);
      return false;
    }
    clearInvalid(input);
    return true;
  },

  textarea(input) {
    if (input.value.length < 10) {
      setInvalid(input);
      return false;
    }
    clearInvalid(input);
    return true;
  },

  select(input) {
    if (input.value === "" || input.value === null) {
      setInvalid(input);
      return false;
    }
    clearInvalid(input);
    return true;
  },

  checkbox(input) {
    if (!input.checked) {
      setInvalid(input);
      return false;
    }
    clearInvalid(input);
    return true;
  },
};

function validateCheckboxGroup(name, form) {
  const checkboxes = $(form).find(
    `input[type="checkbox"][name="${name}"]` // toate checkboxurile au acelasi nume, selectam grupat
  );
  if (!checkboxes.length) return true;

  const fieldset = checkboxes.first().closest("fieldset"); 
  const checked = checkboxes.toArray().some((cb) => cb.checked); // cel putin una selectata

  if (fieldset.length) {
    fieldset.removeClass("invalid");
    if (!checked) fieldset.addClass("invalid");
  }

  return checked;
}

function validateRadioGroup(name, form) {
  const radios = $(form).find(`input[type="radio"][name="${name}"]`);
  if (!radios.length) return true;

  const fieldset = radios.first().closest("fieldset");
  const checked = radios.toArray().some((r) => r.checked);

  if (fieldset.length) {
    fieldset.removeClass("invalid");
    if (!checked) fieldset.addClass("invalid");
  }

  return checked;
}

// skip select validated by cascade

function isCascadeEmpty(select) { // pt field-urile care depind de altele
  return select.options.length <= 1;
}

function validateForm(form) {
  let valid = true;

  $(form).find("input").each(function() {
    const input = this;
    if (input.disabled) return;
    const type = input.type.toLowerCase();
    if (type === "submit" || type === "file" || type === "hidden") return;
    if (type === "radio") return;
    if (type === "checkbox" && input.name !== "agree") return;

    const validator = validators[type];
    if (validator && !validator(input)) valid = false;
  });

  $(form).find("textarea").each(function() {
    if (!validators.textarea(this)) valid = false;
  });

  $(form).find("select").each(function() {
    const select = this;
    if (select.disabled) return;
    if (isCascadeEmpty(select)) return;
    if (!validators.select(select)) valid = false;
  });

  const checkboxGroups = [
    "skill",
    "position",
    "issues",
    "cause",
    "team_ground",
    "team_air",
    "ranger_type",
    "equipment",
    "equipment_category",
  ];
  checkboxGroups.forEach((name) => {
    if (!validateCheckboxGroup(name, form)) valid = false;
  });

  const radioGroups = [
    "difficulty",
    "signage",
    "duration",
    "recommend",
    "hazard_urgency",
  ];
  radioGroups.forEach((name) => {
    if ($(form).find(`input[type="radio"][name="${name}"]`).length) {
      if (!validateRadioGroup(name, form)) valid = false;
    }
  });

  return valid;
}


function attachLiveClearing(form) { // se ataseaza de from si onSubmit e face validarea
  $(form).on("input", function(event) {
    const input = event.target;
    if (!$(input).hasClass("invalid")) return; // daca exista cel putin una invalida returnam

    const type =
      input.tagName === "SELECT"
        ? "select"
        : input.tagName === "TEXTAREA"
          ? "textarea"
          : (input.type || "text").toLowerCase();

    const validator = validators[type];
    if (validator) validator(input);
  });

  $(form).on("change", function(event) {
    const input = event.target;
    if (input.type === "checkbox" || input.type === "radio") {
      clearInvalid(input);
      const label = $(input).closest("label").length ? $(input).closest("label") : $(input).parent();
      if (label.length) clearInvalid(label[0]);
    }
  });
}

$(document).ready(function() {
  $("form").each(function() {
    const form = this;
    attachLiveClearing(form);

    $(form).on("submit", function(event) {
      if (!validateForm(form)) {
        event.preventDefault();
        const firstInvalid = $(form).find(".invalid").first();
        if (firstInvalid.length)
          firstInvalid[0].scrollIntoView({ behavior: "smooth", block: "center" }); // daca exista una invalida onSubmit, scroll there
      }
    });

    $(form).on("change", function(event) {
      const input = event.target;

      if (input.type === "checkbox" || input.type === "radio") {
        const fieldset = $(input).closest("fieldset");

        if (fieldset.length) {
          fieldset.removeClass("invalid");
        }
      }
    });
    
  });
});
