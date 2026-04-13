from fastapi import FastAPI
from app.api.routes import cv_parser

app = FastAPI(title="TunHire AI Service - CV Parser", version="1.0.0")

app.include_router(cv_parser.router, prefix="/v1/cv", tags=["CV Parsing"])

@app.get("/health")
def health_check():
    return {"status": "ok", "service": "ai-service"}
