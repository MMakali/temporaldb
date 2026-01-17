# TemporalDB - Temporal Database System

**A complete temporal database system with CLI and web interface demonstration**

![Status](https://img.shields.io/badge/status-production--ready-brightgreen)
![Java](https://img.shields.io/badge/java-17%2B-blue)
![Spring Boot](https://img.shields.io/badge/spring%20boot-3.1.5-green)
![License](https://img.shields.io/badge/license-MIT-blue)

---

## 📋 Overview

TemporalDB is a complete temporal database system demonstrating **time-travel queries**, **automatic change tracking**, and **complete audit trails**. It includes:

- **Core Module** - classes implementing the temporal database engine
- **Web Module** - Spring Boot web application demo

### What Makes TemporalDB Special

✅ **Temporal Tracking** - Automatically track every change with timestamps  
✅ **Time-Travel Queries** - Query the database as it was at any point in time  
✅ **Audit Trail** - Complete history of all changes  
✅ **Change Detection** - See exactly what changed  
✅ **Zero Configuration** - Just run and use  
✅ **Production Ready** - Enterprise-grade code quality

---

## 🚀 Quick Start (5 Minutes)

### Prerequisites

- **Java 17 or higher** (`java -version`)
- **Maven 3.6+** (`mvn -version`)

### Installation

```bash
# Clone the repository
git clone https://github.com/MMakali/temporaldb.git
cd temporaldb

# Build all modules
mvn clean install
```

### Run the Web Demo (Easiest Way)

```bash
cd web
mvn spring-boot:run
```

Then open: **http://localhost:8080**

You should see:
- Dashboard with inventory statistics
- Products page to manage items
- Audit trail showing all changes
- Time-travel feature to view historical data

### Run the CLI

```bash
java --add-opens java.base/jdk.internal.misc=ALL-UNNAMED \
     -jar target/temporaldb-core-2.0.0.jar
```

Type `help` to see all 40+ commands.

---

## 🎯 Each Module Explained

### Core Module
The temporal database engine with 53+ classes:

**Key Components:**
- TemporalDBServer - Main entry point
- TemporalVersionManager - Tracks all versions
- QueryEngine - Executes temporal queries
- IndexManager - Fast lookups
- CompressionManager - Data compression
- ReplicationManager - Data replication
- BackupManager - Backup/recovery
- MonitoringManager - System metrics
- AuthenticationManager - User security
- TransactionManager - ACID compliance

**Used by:** CLI and Web modules as a library

### CLI Module
Interactive command-line interface with 40+ commands:

**Command Categories:**
```
Connection:  status
Data:        create, drop, insert, select, update, delete
Temporal:    version, asof, snapshot
Index:       index, dropindex, indexes
Replication: replica, replicas, replag
Backup:      backup, backups, restore
Monitor:     metrics, health, stats
Utility:     help, exit, clear, echo
```

**Run it:** `java -jar target/temporaldb-core-2.0.0.jar`

### Web Module
Spring Boot web application demonstrating TemporalDB:

**Pages:**
- Dashboard - Real-time statistics
- Products - Grid view of all products
- Create/Edit - Add and modify products
- Audit Trail - Complete change history
- Product History - Time-travel queries

**API Endpoints:**
- `GET /api/products` - All products
- `GET /api/products/{id}` - Single product
- `GET /api/products/{id}/history` - Product history
- `GET /api/stats` - Inventory statistics

**Run it:** `mvn spring-boot:run` then visit http://localhost:8080

---

## 💡 Real-World Example: Product Inventory

### The Problem
An e-commerce business needs to:
1. Manage products and stock
2. Track price changes
3. Answer historical questions: "What was this product's price on Jan 15?"
4. Maintain audit trail for legal compliance

### Traditional Approach
```
Create Product → Update Price → Query Current Data
Problem: History is lost when data is updated!
```

### TemporalDB Solution
```
Create Product → Automatically tracked with timestamp
Update Price   → Automatically tracked with timestamp
Query current  → Get current data
Query history  → Get data from any point in time
Audit trail    → All changes preserved forever
```

### Code Example
```java
// Create product (tracked automatically)
productService.addProduct(
    new ProductForm("Laptop", "Electronics", 999.99, 5, "High-end")
);
// History recorded: 2024-01-15 14:15:00 - CREATED

// Update price (tracked automatically with change detection)
productService.updateProduct(1,
    new ProductForm("Laptop", "Electronics", 1099.99, 5, "High-end")
);
// History recorded: 2024-01-15 14:20:15 - UPDATED: price 999.99→1099.99

// Time-travel query: "What was the price at 14:17?" (before the update)
Product asOfTime = productService.getProductAsOf(1,
    LocalDateTime.parse("2024-01-15T14:17:00")
);
// Returns: Price 999.99 (as it was before the update)

// Complete audit trail
List<ProductHistory> trail = productService.getAuditTrail();
// Shows all changes with timestamps and what changed
```

---

## 🔄 How Temporal Database Works

### Key Concept: Time-Travel

**Traditional Database:**
```
Now:    Data version 1 (only current data exists)
Update: Data version 2 (version 1 is lost)
Query:  Can only see version 2
```

**Temporal Database:**
```
Time T1: Version 1 (Price: KSH100)
Time T2: Version 2 (Price: KSH120)
Time T3: Version 3 (Price: KSH99)

Query "as of T1": Get Version 1 (Price: KSH100)
Query "as of T2": Get Version 2 (Price: KSH120)
Query "current": Get Version 3 (Price: KSH99)
```

### Three Core Features

1. **Automatic Tracking**
    - Every create/update/delete = timestamp
    - No manual logging needed
    - Completely transparent

2. **Time-Travel Queries**
    - "Show product as of 2024-01-15 at 2 PM"
    - Returns data exactly as it was then
    - Perfect for audits and investigations

3. **Audit Trail**
    - Complete history preserved
    - See who changed what when
    - Compliance-ready logs

---

## 🌐 Web Demo Walkthrough

### 1. Dashboard (Home Page)
Shows real-time inventory statistics:
- Total products (4)
- Total quantity (72 units)
- Total value (KSH1,800)
- Average price (KSH450)
- Changes in last hour (auto-calculated)
- Changes today (auto-calculated)

### 2. Products Page
Grid view of all products with:
- Product name and category
- Price and stock level
- Visual stock bars
- Total inventory value per product
- Quick action buttons (View, Edit)

### 3. Create/Edit Product
Form to add or modify products:
- Product name (required)
- Category dropdown
- Price and quantity
- Description textarea
- Submit button (changes are auto-tracked)

### 4. Audit Trail Page
Complete change history showing:
- Exact timestamp of every change
- Product name (clickable link)
- Change type (Created, Updated, Deleted)
- Current price and quantity at that time
- What changed (e.g., "price 999→1099")
- All entries sorted by time (newest first)

### 5. Product History (Time-Travel)
For each product, you can:
- See complete version history
- Enter a date/time to query
- View product "as of" that moment
- Compare current vs historical data
- See timeline of all versions

---

## 🛠️ Building & Running

### Build All Modules
```bash
# From root directory
mvn clean install
```

### Build Specific Module
```bash
mvn -pl core clean install
mvn -pl web clean install
```

### Run Web Demo
```bash
cd web
mvn spring-boot:run
# Open: http://localhost:8080
```


### Package for Distribution
```bash
mvn clean package
# Creates JAR files in target/ directories
```

---


## 🔧 Configuration

### Web Module Config
File: `web/src/main/resources/application.properties`

```properties
# Server
server.port=8080
server.servlet.context-path=/

# Thymeleaf template engine
spring.thymeleaf.cache=false
spring.thymeleaf.mode=HTML

# Logging
logging.level.root=INFO
logging.level.com.temporaldb=DEBUG
```


### Core Module Config
Configured programmatically when instantiated.

---

## 🔐 Security Features

- ✅ Input validation on all user inputs
- ✅ SQL injection protection (parameterized queries)
- ✅ CSRF protection (Spring Security ready)
- ✅ Secure session handling
- ✅ Audit logging for compliance
- ✅ No hardcoded credentials
- ✅ Error handling without exposing internals

---


### Slow Startup
This is normal - Spring Boot initializes all components on startup. First run takes 3-5 seconds.

### Maven Build Failures
```bash
# Update dependencies
mvn clean install -U

# Clear Maven cache
rm -rf ~/.m2/repository
mvn clean install
```

## 💡 Tips & Tricks

1. **Use curl to query the API:**
   ```bash
   curl http://localhost:8080/api/products | jq
   curl http://localhost:8080/api/stats | jq
   ```

2. **Export data as CSV:**
   ```bash
   curl http://localhost:8080/api/products | jq -r '.[] | [.name, .price, .quantity] | @csv'
   ```

3. **Monitor in real-time:**
    - Open audit trail page
    - Make changes in another tab
    - Refresh to see new entries

4. **Test time-travel:**
    - Create product at 10:00
    - Update price at 11:00
    - Query "as of 10:30" to see original price

---


## 📝 License

MIT License - See LICENSE file for details

---


## ✅ Pre-Flight Checklist

Before you start:

- [ ] Java 17+ installed
- [ ] Maven 3.6+ installed
- [ ] Git installed
- [ ] 2GB free disk space
- [ ] Port 8080 available
- [ ] Internet connection (for Maven dependencies)

---

## 🎉 You're Ready!

Everything is set up and ready to use:

1. **Clone repo**
2. **Run `mvn clean install`**
3. **Run `cd web && mvn spring-boot:run`**
4. **Open http://localhost:8080**
5. **Start creating and tracking products!**

---

## Quick Reference

```bash
# Clone
git clone https://github.com/Mmakali/temporaldb.git
cd temporaldb

# Build
mvn clean install

# Run Web Demo
cd web && mvn spring-boot:run
# → http://localhost:8080



# Clean Build
mvn clean install -U

# View Help
java -jar target/temporaldb-core-2.0.0.jar
# Type: help
```

---
