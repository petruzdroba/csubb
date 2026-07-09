from transformers import GPT2Tokenizer, GPT2LMHeadModel
import torch
import re
from nltk.translate.bleu_score import sentence_bleu, SmoothingFunction
from nltk.tokenize import word_tokenize

MODEL_PATH = "./gpt2-poem-sentiment"

with open("data/poem2.txt", "r", encoding="utf-8") as f:
    first_lines = [line.strip() for line in f if line.strip()]

with open("data/poem2.txt", "r", encoding="utf-8") as f:
    references = [word_tokenize(line.strip().lower()) for line in f if line.strip()]

CONFIGS = [
    {
        "label": "Greedy (baseline)",
        "do_sample": False,
        "num_beams": 1,
        "max_new_tokens": 60,
        "repetition_penalty": 1.0
    },
    {
        "label": "Beam Search — beams=4",
        "do_sample": False,
        "num_beams": 4,
        "max_new_tokens": 100,
        "repetition_penalty": 1.2
    },
    {
        "label": "Sampling — balanced",
        "do_sample": True,
        "num_beams": 1,
        "max_new_tokens": 140,
        "temperature": 0.8,
        "top_p": 0.92,
        "top_k": 40,
        "repetition_penalty": 1.2
    },
    {
        "label": "CREATIVE (controlled)",
        "do_sample": True,
        "num_beams": 1,
        "max_new_tokens": 140,
        "temperature": 1.0,
        "top_p": 0.95,
        "top_k": 50,
        "repetition_penalty": 1.3,
        "no_repeat_ngram_size": 3
    }
]

tokenizer = GPT2Tokenizer.from_pretrained(MODEL_PATH)

tokenizer.pad_token = tokenizer.eos_token

model = GPT2LMHeadModel.from_pretrained(MODEL_PATH)
model.eval()

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model.to(device)

bad_words_ids = [
    tokenizer.encode("Copyright", add_special_tokens=False),
    tokenizer.encode("Comments", add_special_tokens=False),
    tokenizer.encode("Follow", add_special_tokens=False),
    tokenizer.encode("Advertisement", add_special_tokens=False),
]

for i, line in enumerate(first_lines[:20]):
    cfg = CONFIGS[i % len(CONFIGS)]

    prompt = f"{line}\n"

    inputs = tokenizer(prompt, return_tensors="pt").to(device)

    gen_kwargs = dict(
        input_ids=inputs["input_ids"],
        attention_mask=inputs["attention_mask"],

        max_new_tokens=cfg["max_new_tokens"],
        do_sample=cfg["do_sample"],
        num_beams=cfg["num_beams"],

        repetition_penalty=cfg["repetition_penalty"],
        no_repeat_ngram_size=3,

        pad_token_id=tokenizer.eos_token_id,
        eos_token_id=tokenizer.eos_token_id,

        bad_words_ids=bad_words_ids,
        early_stopping=True
    )

    if cfg["do_sample"]:
        gen_kwargs["temperature"] = cfg.get("temperature", 1.0)
        gen_kwargs["top_p"] = cfg.get("top_p", 0.9)
        gen_kwargs["top_k"] = cfg.get("top_k", 50)

    output = model.generate(**gen_kwargs)

    generated = tokenizer.decode(
        output[0][inputs["input_ids"].shape[1]:],
        skip_special_tokens=True
    )
    
    generated = re.sub(r"[-_]{2,}", "", generated)
    # generated = re.sub(r"(Copyright|Comments|Follow|Advertisement).*", "", generated)
    generated = re.sub(r"\s{2,}", " ", generated).strip()
    
    generated_tokens = word_tokenize(generated.lower())
    bleu = sentence_bleu(references, generated_tokens, smoothing_function=SmoothingFunction().method1)

    print(f"\nPoem {i+1} — {cfg['label']}")
    print(f"Input:     {line}")
    print(f"Generated: {generated}")
    print(f"BLEU Score: {bleu:.4f}")