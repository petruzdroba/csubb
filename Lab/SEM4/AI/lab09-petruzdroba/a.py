from transformers import GPT2LMHeadModel, GPT2Tokenizer
from nltk.translate.bleu_score import sentence_bleu, SmoothingFunction
from nltk.tokenize import word_tokenize

with open("data/poem2.txt", "r", encoding="utf-8") as f:
    first_lines = [line.strip() for line in f if line.strip()]

with open("data/poem2.txt", "r", encoding="utf-8") as f:
    references = [word_tokenize(line.strip().lower()) for line in f if line.strip()]

CONFIGS = [
    {
        "label": "Greedy (baseline)",
        "do_sample": False, "num_beams": 1,
        "max_new_tokens": 30, "repetition_penalty": 1.0,
    },
    {
        "label": "Beam Search — beams=4",
        "do_sample": False, "num_beams": 4,
        "max_new_tokens": 60, "repetition_penalty": 1.0,
    },
    {
        "label": "Sampling — temperature=0.5, top_p=0.9",
        "do_sample": True, "num_beams": 1,
        "max_new_tokens": 90, "temperature": 0.5,
        "top_p": 0.9, "repetition_penalty": 1.5,
    },
    {
        "label": "Sampling — temperature=1.4, top_p=0.95",
        "do_sample": True, "num_beams": 1,
        "max_new_tokens": 120, "temperature": 1.4,
        "top_p": 0.95, "repetition_penalty": 1.5,
    },
    {
        "label": "Top-k sampling — k=50",
        "do_sample": True, "num_beams": 1,
        "max_new_tokens": 150, "temperature": 1.0,
        "top_k": 50, "repetition_penalty": 1.5,
    },
    {
        "label": "Beam Search — beams=4, repetition_penalty=1.5",
        "do_sample": False, "num_beams": 4,
        "max_new_tokens": 200, "repetition_penalty": 1.5,
    },
]

tokenizer = GPT2Tokenizer.from_pretrained("gpt2")
tokenizer.pad_token = tokenizer.eos_token
model = GPT2LMHeadModel.from_pretrained("gpt2")
model.eval()

for i, line in enumerate(first_lines):
    config = CONFIGS[i % len(CONFIGS)]

    prompt = f"Continue this poem:\n{line}\n"
    inputs = tokenizer(prompt, return_tensors="pt", padding=True)

    gen_kwargs = {
        "input_ids":          inputs["input_ids"],
        "attention_mask":     inputs["attention_mask"],
        "max_new_tokens":     config["max_new_tokens"],
        "do_sample":          config["do_sample"],
        "num_beams":          config["num_beams"],
        "repetition_penalty": config["repetition_penalty"],
        "pad_token_id":       tokenizer.eos_token_id,
    }
    if config.get("temperature") and config["do_sample"]:
        gen_kwargs["temperature"] = config["temperature"]
    if config.get("top_p"):
        gen_kwargs["top_p"] = config["top_p"]
    if config.get("top_k"):
        gen_kwargs["top_k"] = config["top_k"]

    output_ids = model.generate(**gen_kwargs)
    generated = tokenizer.decode(
        output_ids[0][inputs["input_ids"].shape[1]:],
        skip_special_tokens=True
    )
    
    generated_tokens = word_tokenize(generated.lower())
    bleu = sentence_bleu(references, generated_tokens, smoothing_function=SmoothingFunction().method1)

    print(f"Poem {i+1} — {config['label']} — max_new_tokens={config['max_new_tokens']}")
    print(f"Input:     {line}")
    print(f"Generated: {generated}")
    print(f"BLEU Score: {bleu:.4f}")
    print()