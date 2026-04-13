from fastapi import APIRouter, UploadFile, File, HTTPException
from app.schemas.cv import CVParseResult
from app.utils.text_extractor import extract_text_from_file
from app.services.parsing_service import parse_cv_text

router = APIRouter()

@router.post("/parse", response_model=CVParseResult)
async def parse_cv(file: UploadFile = File(...)):
    if not file.filename.endswith((".pdf", ".docx")):
        raise HTTPException(status_code=400, detail="Only PDF and DOCX files are allowed.")
    
    try:
        raw_text = await extract_text_from_file(file)
        parsed_data = parse_cv_text(raw_text)
        
        return CVParseResult(
            full_name=parsed_data.get("full_name", ""),
            email=parsed_data.get("email", ""),
            phone=parsed_data.get("phone"),
            location=parsed_data.get("location"),
            years_experience=parsed_data.get("years_experience", 0),
            skills=parsed_data.get("skills", []),
            education=parsed_data.get("education", []),
            raw_text=raw_text,
            parser_version="1.0.0",
            confidence_score=parsed_data.get("confidence_score", 0.0)
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
