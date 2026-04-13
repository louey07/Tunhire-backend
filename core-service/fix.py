import os
files = [
    r'src\main\java\com\tunhire\tunhire\candidate\entity\CandidateProfile.java',
    r'src\main\java\com\tunhire\tunhire\candidate\entity\CandidateSkill.java',
    r'src\main\java\com\tunhire\tunhire\recruiter\entity\CompanyMembership.java'
]
imports = '''import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
'''
for f in files:
    with open(f, 'r', encoding='utf-8') as file:
        content = file.read()
    content = content.replace('import jakarta.persistence.*;', 'import jakarta.persistence.*;\n' + imports)
    with open(f, 'w', encoding='utf-8') as file:
        file.write(content)
print('Fixed!')
