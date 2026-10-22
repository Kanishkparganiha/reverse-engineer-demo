import csv
import random
from datetime import datetime, timedelta
from faker import Faker

# Initialize the Faker generator
fake = Faker()

# Configuration parameters
num_rows = 100
filename = "input.csv"

statuses = ["ACTIVE", "INACTIVE"]
region_codes = [1, 2, 3, 4, 5]

# Date range for signup_date (MM/DD/YYYY format)
start_date = datetime(2020, 1, 1)
end_date = datetime(2026, 12, 31)
date_delta = (end_date - start_date).days

rows = []

# 1. Write the header matching your CSV structure
rows.append(["customer_id", "status", "signup_date", "region_code", "name"])

# 2. Generate random rows
for i in range(1, num_rows + 1):
    # ~5% chance to leave customer_id empty, mimicking your sample data
    customer_id = "" if random.random() < 0.05 else str(100 + i)
    
    status = random.choice(statuses)
    
    # Generate a random date within the range
    random_days = random.randrange(date_delta)
    signup_date = (start_date + timedelta(days=random_days)).strftime("%m/%d/%Y")
    
    region_code = random.choice(region_codes)
    name = fake.name()  # Generates a completely randomized, realistic name each time
    
    rows.append([customer_id, status, signup_date, region_code, name])

# 3. Save to CSV file
with open(filename, mode="w", newline="", encoding="utf-8") as f:
    writer = csv.writer(f)
    writer.writerows(rows)

print(f"Successfully generated {num_rows} rows with Faker and saved to '{filename}'!")