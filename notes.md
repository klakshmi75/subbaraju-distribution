1. Got some error while starting mysql after installing it using community server. Then uninstalled and tried web version which downloads content on the fly while installing. This was successful. I had to manually retry some steps.
2. Faced issue with converting string to LocalDate in TripsRequest object. Had to add Date serializer and deserializer
explicitly in UtilBeansConfig. @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) did not help.
3. Spring bean validations did not work properly. @NotNull worked but custom validations using @AssertTue did not work. The reason was method naming convention. method names should start with is or get. 
Added spring-boot-starter-validation. hibernate-validator need not be added explicitly.
4. Faced issue with apache poi dependency versions. Unable to import XSSFWorkbook class. Changing the version fixed the issue.

