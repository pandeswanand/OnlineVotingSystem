import { Component } from "@angular/core";
import { AuthenticationService } from "../_service/app.authenticationservice";
import { Router } from "@angular/router";
import { User } from "../_model/app.user";
import { Poll } from "../_model/app.poll";

@Component({
    selector:"home",
    templateUrl: "../_html/app.home.html"
})
export class Homecomponent {

    emailId:string;
    password:string;
    user:User;

    constructor(private service:AuthenticationService, private router:Router){}

    passwordError=null;
    validatePassword():boolean{
        var pattern = new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/i);
        if(this.password==null){
            this.passwordError="Password cannot be empty!";
            return false;
        }
        else if(!(pattern.test(this.password))){
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
        if(this.emailId==null){
            this.emailError="Email cannot be empty!";
            return false;
        }
        else{
            let atpos = this.emailId.indexOf("@");
            var dotpos = this.emailId.lastIndexOf(".");
            if (atpos<1 || dotpos<atpos+2 || dotpos+2>=this.emailId.length){
                this.emailError="Invalid Email format!";
                return false;
            }
            else{
                this.emailError=null;
                return true;
            }
        }
    }

    login(){
        if(this.validateEmail() && this.validatePassword()){
            this.service.authenticate(this.emailId, this.password).subscribe(
                userData => {
                    let tokenStr= 'Bearer '+ userData.token;
                    sessionStorage.setItem('token', tokenStr);
                    sessionStorage.setItem('userEmail',this.emailId);
                    this.service.checkRole(this.emailId).subscribe((data:any)=>{this.user=data;this.checkRoles();});
                }, error=>{
                    alert("Invalid Credentials");
                    this.router.navigate(['/home']);
                }
            );
        }
    }

    checkRoles(){
        if(this.user.isAdmin){
            sessionStorage.setItem('role',"admin");
            sessionStorage.setItem("userId",this.user.userId.toString());
            sessionStorage.setItem("location", this.user.pollLocation);
            this.router.navigate(['/admin']).then(() => {
                window.location.reload();
            });
        
        }
        else{
            sessionStorage.setItem('role',"user");
            sessionStorage.setItem("userId",this.user.userId.toString());
            sessionStorage.setItem("location", this.user.pollLocation);
            this.router.navigate(['/user']).then(() => {
                window.location.reload();
            });
        }
    }
}