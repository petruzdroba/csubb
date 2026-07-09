from datasets import load_dataset
from transformers import (
    GPT2Tokenizer,
    GPT2LMHeadModel,
    Trainer,
    TrainingArguments,
    DataCollatorForLanguageModeling
)
import torch

MODEL_NAME = "gpt2"
OUTPUT_DIR = "./gpt2-poem-sentiment"


dataset = load_dataset("poem_sentiment")

def extract(example):
    return {"text": example["verse_text"]}

dataset = dataset.map(extract)


def clean(example):
    t = example["text"]
    if len(t.split()) < 5:
        return False
    if len(t) > 500:
        return False
    return True

dataset = dataset.filter(clean)

dataset = dataset["train"].shuffle(seed=42)


tokenizer = GPT2Tokenizer.from_pretrained(MODEL_NAME)
tokenizer.pad_token = tokenizer.eos_token

def tokenize(batch):
    return tokenizer(
        batch["text"],
        truncation=True,
        padding=False,
        max_length=128
    )

tokenized = dataset.map(tokenize, batched=True, remove_columns=["text"])

model = GPT2LMHeadModel.from_pretrained(MODEL_NAME)
model.config.pad_token_id = tokenizer.eos_token_id


training_args = TrainingArguments(
    output_dir=OUTPUT_DIR,

    num_train_epochs=3,
    per_device_train_batch_size=2,
    gradient_accumulation_steps=4,

    learning_rate=5e-5,
    warmup_steps=50,

    logging_steps=20,
    save_steps=200,

    do_eval=False, 

    report_to="none",
    fp16=torch.cuda.is_available()
)

data_collator = DataCollatorForLanguageModeling(
    tokenizer=tokenizer,
    mlm=False
)

trainer = Trainer(
    model=model,
    args=training_args,
    train_dataset=tokenized,
    data_collator=data_collator
)

trainer.train()

trainer.save_model(OUTPUT_DIR)
tokenizer.save_pretrained(OUTPUT_DIR)

print("DONE -> saved to", OUTPUT_DIR)