import re
import spacy
from typing import List, Dict, Any

# Load spacy model if available, fallback to basic logic
try:
    nlp = spacy.load("fr_core_news_md")
except OSError:
    nlp = None

def parse_cv_text(text: str) -> Dict[str, Any]:
    lines = text.split('\n')
    
    email = _extract_email(text)
    phone = _extract_phone(text)
    full_name = lines[0] if lines else "Unknown" # Basic fallback
    
    # Very basic placeholder logic for other fields
    skills = []
    education = []
    years_experience = 0
    location = None
    
    # Find experience
    exp_match = re.search(r'(\d+)\s+ans\s*d[\'’]?exp[ée]rience', text, re.IGNORECASE)
    if exp_match:
        years_experience = int(exp_match.group(1))
        
    # Basic skill extraction attempt based on common keywords
    if "COMPETENCES" in text.upper() or "SKILLS" in text.upper():
        # Note: full logic would extract exactly the skills block. 
        # Here we just provide a placeholder to ensure the system is end-to-end runnable.
        pass
        
    return {
        "full_name": full_name,
        "email": email or "",
        "phone": phone,
        "location": location,
        "years_experience": years_experience,
        "skills": skills,
        "education": education,
        "raw_text": text,
        "confidence_score": 0.8
    }

def _extract_email(text: str) -> str:
    match = re.search(r'[\w.+-]+@[\w-]+\.[\w.]+', text)
    return match.group(0) if match else None

def _extract_phone(text: str) -> str:
    match = re.search(r'(?:\+216\s*)?[24579]\d\s*\d{2}\s*\d{3}|\+216\s*\d{8}', text)
    return match.group(0) if match else None
