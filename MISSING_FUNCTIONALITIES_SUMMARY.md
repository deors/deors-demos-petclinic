# Missing Functionalities Summary - Pet Clinic Application

## Quick Reference Guide

### CRUD Implementation Status

| Entity | Create | Read Single | Read List | Update | Delete | Status |
|--------|--------|-------------|-----------|--------|--------|--------|
| **Owner** | ✓ | ✓ | ✓ | ✓ | ✓ | ✅ COMPLETE |
| **Pet** | ✓ | ⚠️ | ⚠️ | ✓ | ✓ | ⚠️ MOSTLY COMPLETE |
| **Visit** | ✓ | ✗ | ✓ | ✗ | ⚠️ | ❌ INCOMPLETE |
| **Vet** | ✗ | ✗ | ✓ | ✗ | ✗ | ❌ READ-ONLY |
| **PetType** | ✗ | ✗ | ✓ | ✗ | ✗ | ❌ READ-ONLY |
| **Specialty** | ✗ | ✗ | ✗ | ✗ | ✗ | ❌ NO MANAGEMENT |

**Legend**:
- ✓ Fully implemented
- ⚠️ Partially implemented or lacking endpoint
- ✗ Not implemented
- ✅ Complete / ⚠️ Mostly Complete / ❌ Incomplete

---

## Critical Missing Functionalities

### 1. Visit Entity (CRITICAL - Breaks CRUD Pattern)

#### Missing Service Methods
- `Visit loadVisit(int id)` - Load a single visit by ID

#### Missing Web Endpoints
```
GET    /visits/{visitId}                          - View single visit
GET    /owners/*/pets/*/visits/{visitId}/edit    - Edit visit form
PUT    /owners/*/pets/*/visits/{visitId}/edit    - Update visit
DELETE /owners/*/pets/*/visits/{visitId}/edit    - Delete visit
```

#### Implementation Issues
- `storeVisit()` throws `UnsupportedOperationException` for updates
- Delete method exists in service but no web endpoint

---

### 2. Vet Entity (HIGH - No Management Capability)

#### Missing Service Methods
```java
Vet loadVet(int id)
void storeVet(Vet vet)
void deleteVet(int id)
Collection<Vet> findVets(String lastName)
```

#### Missing Web Endpoints
```
GET    /vets/{vetId}            - View single vet
GET    /vets/new               - Create vet form
POST   /vets/new               - Create vet submit
GET    /vets/{vetId}/edit      - Edit vet form
PUT    /vets/{vetId}/edit      - Update vet
DELETE /vets/{vetId}/edit      - Delete vet
GET    /vets/search            - Search vets form
```

---

### 3. Pet Entity (MEDIUM - Missing Conveniences)

#### Missing Web Endpoints
```
GET    /pets/{petId}           - View single pet (currently only via owner page)
GET    /pets                   - List all pets
GET    /pets?type={typeId}     - Filter by pet type
```

---

### 4. PetType Entity (MEDIUM - Admin Feature)

#### Missing Service Methods
```java
PetType loadPetType(int id)
void storePetType(PetType type)
void deletePetType(int id)
```

#### Missing Web Endpoints
```
GET    /pettypes               - List all pet types
GET    /pettypes/{typeId}      - View single pet type
GET    /pettypes/new          - Create pet type form
POST   /pettypes/new          - Create pet type submit
GET    /pettypes/{typeId}/edit - Edit pet type form
PUT    /pettypes/{typeId}/edit - Update pet type
DELETE /pettypes/{typeId}/edit - Delete pet type
```

---

### 5. Specialty Entity (MEDIUM - Admin Feature)

#### Missing Service Methods
```java
Collection<Specialty> getSpecialties()
Specialty loadSpecialty(int id)
void storeSpecialty(Specialty specialty)
void deleteSpecialty(int id)
void addSpecialtyToVet(int vetId, int specialtyId)
void removeSpecialtyFromVet(int vetId, int specialtyId)
```

#### Missing Web Endpoints
```
GET    /specialties                    - List all specialties
GET    /specialties/{specialtyId}      - View single specialty
GET    /specialties/new               - Create specialty form
POST   /specialties/new               - Create specialty submit
GET    /specialties/{specialtyId}/edit - Edit specialty form
PUT    /specialties/{specialtyId}/edit - Update specialty
DELETE /specialties/{specialtyId}/edit - Delete specialty
```

---

## REST API - Completely Missing

The application currently has NO RESTful JSON API. Only traditional MVC with HTML views.

### Required REST Controllers

#### Owner API
```
GET    /api/owners                - List all owners
GET    /api/owners?lastName={name} - Search owners
GET    /api/owners/{id}           - Get owner by ID
POST   /api/owners                - Create owner (JSON body)
PUT    /api/owners/{id}           - Update owner (JSON body)
DELETE /api/owners/{id}           - Delete owner
```

#### Pet API
```
GET    /api/pets                  - List all pets
GET    /api/pets/{id}             - Get pet by ID
GET    /api/pets?type={typeId}    - Filter by type
POST   /api/pets                  - Create pet (JSON body)
PUT    /api/pets/{id}             - Update pet (JSON body)
DELETE /api/pets/{id}             - Delete pet
```

#### Visit API
```
GET    /api/visits                - List all visits
GET    /api/visits/{id}           - Get visit by ID
GET    /api/visits?from={date}&to={date} - Filter by date range
POST   /api/visits                - Create visit (JSON body)
PUT    /api/visits/{id}           - Update visit (JSON body)
DELETE /api/visits/{id}           - Delete visit
```

#### Vet API
```
GET    /api/vets                  - List all vets
GET    /api/vets/{id}             - Get vet by ID
GET    /api/vets?specialty={id}   - Filter by specialty
POST   /api/vets                  - Create vet (JSON body)
PUT    /api/vets/{id}             - Update vet (JSON body)
DELETE /api/vets/{id}             - Delete vet
```

#### PetType API
```
GET    /api/pettypes              - List all pet types
GET    /api/pettypes/{id}         - Get pet type by ID
POST   /api/pettypes              - Create pet type (JSON body)
PUT    /api/pettypes/{id}         - Update pet type (JSON body)
DELETE /api/pettypes/{id}         - Delete pet type
```

#### Specialty API
```
GET    /api/specialties           - List all specialties
GET    /api/specialties/{id}      - Get specialty by ID
POST   /api/specialties           - Create specialty (JSON body)
PUT    /api/specialties/{id}      - Update specialty (JSON body)
DELETE /api/specialties/{id}      - Delete specialty
```

---

## Advanced Features Missing

### Search and Filter
- ✗ Search visits by date range
- ✗ Search vets by specialty
- ✗ Filter pets by owner
- ✗ Advanced owner search (by city, phone, etc.)

### Reporting
- ✗ Visit history reports
- ✗ Popular pet types statistics
- ✗ Vet workload reports
- ✗ Revenue reports (if applicable)

### Bulk Operations
- ✗ Bulk import owners/pets
- ✗ Export data to CSV/Excel
- ✗ Batch updates

### API Features
- ✗ API documentation (Swagger/OpenAPI)
- ✗ HATEOAS links
- ✗ API versioning
- ✗ Rate limiting
- ✗ API authentication/authorization

---

## Implementation Priority

### Phase 1: Complete Core CRUD (1-2 weeks)
**Goal**: Make all entities fully manageable

1. ✗ Fix Visit update functionality
2. ✗ Add EditVisitForm controller
3. ✗ Add Visit delete endpoint
4. ✗ Add loadVisit() service method
5. ✗ Add dedicated Pet view page
6. ✗ Implement Vet CRUD operations
7. ✗ Add VetController endpoints

**Files to Create/Modify**:
- `src/main/java/org/springframework/samples/petclinic/web/EditVisitForm.java` (NEW)
- `src/main/java/org/springframework/samples/petclinic/web/AddVetForm.java` (NEW)
- `src/main/java/org/springframework/samples/petclinic/web/EditVetForm.java` (NEW)
- `src/main/java/org/springframework/samples/petclinic/Clinic.java` (MODIFY)
- `src/main/java/org/springframework/samples/petclinic/jdbc/SimpleJdbcClinic.java` (MODIFY)
- Similar changes in JPA and Hibernate implementations

### Phase 2: Reference Data Management (1 week)
**Goal**: Allow administrators to manage pet types and specialties

1. ✗ Implement PetType CRUD
2. ✗ Implement Specialty CRUD
3. ✗ Add admin controllers

**Files to Create**:
- `src/main/java/org/springframework/samples/petclinic/web/PetTypeController.java`
- `src/main/java/org/springframework/samples/petclinic/web/SpecialtyController.java`

### Phase 3: REST API (2-3 weeks)
**Goal**: Provide modern RESTful API

1. ✗ Create REST controllers for all entities
2. ✗ Add JSON serialization support
3. ✗ Implement API documentation
4. ✗ Add error handling for APIs

**Files to Create**:
- `src/main/java/org/springframework/samples/petclinic/api/OwnerRestController.java`
- `src/main/java/org/springframework/samples/petclinic/api/PetRestController.java`
- `src/main/java/org/springframework/samples/petclinic/api/VisitRestController.java`
- `src/main/java/org/springframework/samples/petclinic/api/VetRestController.java`
- Similar files for other entities

### Phase 4: Advanced Features (3-4 weeks)
**Goal**: Add search, filtering, and reporting

1. ✗ Advanced search capabilities
2. ✗ Reporting endpoints
3. ✗ Bulk operations
4. ✗ Data export features

---

## Testing Requirements

### Unit Tests Needed
- Visit update operations
- Vet CRUD operations
- PetType CRUD operations
- Specialty CRUD operations
- All REST controllers

### Integration Tests Needed
- Complete Visit workflow (create, view, update, delete)
- Complete Vet workflow
- API endpoint testing
- Error handling testing

### Current Test Coverage
- ✓ Owner CRUD (complete)
- ✓ Pet CRUD (mostly complete)
- ✓ Visit create (complete)
- ✗ Visit update (missing)
- ✗ Visit delete (missing)
- ✗ Vet CRUD (missing)
- ✗ PetType CRUD (missing)
- ✗ Specialty CRUD (missing)

---

## Estimated Effort

| Phase | Effort | Items | Priority |
|-------|--------|-------|----------|
| Phase 1: Core CRUD | 1-2 weeks | 7 items | CRITICAL |
| Phase 2: Reference Data | 1 week | 2 entities | HIGH |
| Phase 3: REST API | 2-3 weeks | 6 REST controllers | MEDIUM |
| Phase 4: Advanced Features | 3-4 weeks | Multiple features | LOW |
| **Total** | **7-10 weeks** | **~40 functionalities** | - |

---

## Conclusion

**Current State**: 
- 1 entity fully complete (Owner)
- 2 entities mostly complete (Pet)
- 3 entities incomplete or read-only (Visit, Vet, PetType)
- 1 entity with no management (Specialty)
- 0 REST API endpoints

**Target State**:
- All 6 entities with complete CRUD
- Full REST API with JSON support
- Advanced search and filtering
- Complete test coverage

**Gap**: ~40 missing functionalities across all layers (service, controller, REST API)

For detailed analysis, see: [CRUD_ANALYSIS.md](./CRUD_ANALYSIS.md)
