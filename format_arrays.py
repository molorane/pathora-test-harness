import os
import re

dir_path = 'src/test/java/za/co/pathora/testharness/engine/operator'
pattern = re.compile(r'\"\"\"\n(\s*)\[(.*?)\]\n(\s*)\"\"\"', re.DOTALL)

def reformat_json_array(match):
    indent = match.group(1)
    content = match.group(2)
    closing_indent = match.group(3)
    
    # Don't reformat complex objects or nested arrays
    if '{' in content or '}' in content or '[' in content or ']' in content:
        return match.group(0)
    
    items = [item.strip() for item in content.split(',') if item.strip()]
    formatted_content = ", ".join(items)
    
    if not items:
        return f'"""\n{indent}[]\n{closing_indent}"""'
        
    return f'"""\n{indent}[{formatted_content}]\n{closing_indent}"""'

for filename in os.listdir(dir_path):
    if filename.endswith('Test.java'):
        filepath = os.path.join(dir_path, filename)
        with open(filepath, 'r') as f:
            content = f.read()
        
        new_content = pattern.sub(reformat_json_array, content)
        
        if new_content != content:
            with open(filepath, 'w') as f:
                f.write(new_content)
            print(f"Updated {filename}")
