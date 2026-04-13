import io
import pdfplumber
import docx
from fastapi import UploadFile

async def extract_text_from_file(file: UploadFile) -> str:
    content = await file.read()
    filename = file.filename.lower()
    
    raw_text = ""
    
    if filename.endswith(".pdf"):
        with pdfplumber.open(io.BytesIO(content)) as pdf:
            for page in pdf.pages:
                page_text = page.extract_text()
                if page_text:
                    raw_text += page_text + "\n"
    elif filename.endswith(".docx"):
        doc = docx.Document(io.BytesIO(content))
        for para in doc.paragraphs:
            raw_text += para.text + "\n"
        for table in doc.tables:
            for row in table.rows:
                for cell in row.cells:
                    raw_text += cell.text + "\n"
    else:
        raise ValueError("Unsupported file format. Please upload a PDF or DOCX file.")
        
    # Clean up empty lines
    lines = [line.strip() for line in raw_text.split('\n') if line.strip()]
    return "\n".join(lines)
