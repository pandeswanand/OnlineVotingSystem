import { Component } from "@angular/core";
import { UserService } from "../_service/app.userservice";
import { AuthenticationService } from "../_service/app.authenticationservice";
import { User } from "../_model/app.user";
import { Address } from "../_model/app.address";
import { Router } from "@angular/router";

@Component({
    selector:'register',
    templateUrl:'../_html/app.register.html'
})
export class RegisterComponent{
    userdata:any={
        address:{}
    };
    constructor(private service:AuthenticationService, private router:Router){}

    nameError=null;
    validateName():boolean{
        if(this.userdata.username == null){
            this.nameError = "Username cannot be empty!";
            return false;
        }
        else{
            this.nameError = null;
            return true;
        }
    }

    passwordError=null;
    validatePassword():boolean{
        var pattern = new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/i);
        if(this.userdata.password==null){
            this.passwordError="Password cannot be empty!";
            return false;
        }
        else if(!(pattern.test(this.userdata.password))){
            this.passwordError="Password should contain atleast one Upper case alphabet, one lower case, one numeric character, one special character and length should be greater than 8 characters long!";
            return false;
        }
        else{
            this.passwordError=null;
            return true;
        }
    }

    emailError=null;
    validateEmail():boolean{
        if(this.userdata.emailId==null){
            this.emailError="Email cannot be empty!";
            return false;
        }
        else{
            let atpos = this.userdata.emailId.indexOf("@");
            var dotpos = this.userdata.emailId.lastIndexOf(".");
            if (atpos<1 || dotpos<atpos+2 || dotpos+2>=this.userdata.emailId.length){
                this.emailError="Invalid Email format!";
                return false;
            }
            else{
                this.emailError=null;
                return true;
            }
        }
    }

    ageError=null;
    validateAge():boolean{
        if(this.userdata.age == null){
            this.ageError="Age cannot be empty!";
            return false;
        }
        else if(this.userdata.age<18){
            this.ageError="Age cannot be less than 18 years!";
            return false;
        }
        else{
            this.ageError=null;
            return true;
        }
    }

    aadharError=null;
    validateAadhar():boolean{
        if(this.userdata.aadharNo == null){
            this.aadharError="Aadhar number cannot be empty!";
            return false;
        }
        else if(this.userdata.aadharNo.toString().length != 12){
            this.aadharError="Aadhar number should be 12 digits long!";
            return false;
        }
        else{
            this.aadharError=null;
            return true;
        }
    }

    genderError=null;
    validateGender():boolean{
        if(this.userdata.gender == null){
            this.genderError="Gender cannot be empty!";
            return false;
        }
        else{
            this.genderError=null;
            return true;
        }
    }

    stateError=null;
    validateState():boolean{
        if(this.userdata.address.state==null){
            this.stateError="State cannot be empty!";
            return false;
        }
        else{
            this.stateError=null;
            return true;
        }
    }

    cityError=null;
    validateCity():boolean{
        if(this.userdata.address.city==null){
            this.cityError="City cannot be empty!";
            return false;
        }
        else{
            this.cityError=null;
            return true;
        }
    }

    areaError=null;
    validateArea():boolean{
        if(this.userdata.address.area==null){
            this.areaError="Area cannot be empty!";
            return false;
        }
        else{
            this.areaError=null;
            return true;
        }
    }

    pincodeError=null;
    validatePincode():boolean{
        if(this.userdata.address.pincode==null){
            this.pincodeError="Pincode cannot be empty!";
            return false;
        }
        else if(this.userdata.address.pincode.toString().length != 6){
            this.pincodeError="Pincode should be 6 digits long!";
            return false;
        }
        else{
            this.pincodeError=null;
            return true;
        }
    }

    register(){
        if(this.validateName() && this.validatePassword() && this.validateEmail() && this.validateAge() && this.validateAadhar() && this.validateGender() && this.validateState() && this.validateCity() && this.validateArea() && this.validatePincode()){
            this.service.register(this.userdata).subscribe((data)=>{alert("Successfully Registered!");
                this.router.navigate(['/home']).then(()=>window.location.reload())}, error=>{console.log(error.error);});
        }
    }
}