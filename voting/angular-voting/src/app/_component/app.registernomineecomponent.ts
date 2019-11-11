import { Component, OnInit } from "@angular/core";
import { UserService } from "../_service/app.userservice";
import { Router } from "@angular/router";

@Component({
    selector:'registernominee',
    templateUrl:'../_html/app.registernominee.html'
})
export class RegisterNomineeComponent implements OnInit{

    area:string;

    constructor(private service:UserService, private router:Router){}

    centerError=null;
    validateCenter():boolean{
        if(this.area==null){
            this.centerError="Area cannot be empty!";
            return false;
        }
        else{
            this.centerError=null;
            return true;
        }
    }

    ngOnInit(){
        if(sessionStorage.getItem("role")!= "user"){
            this.router.navigate(['/error403'])
        }
    }

    registerNominee(){
        if(this.validateCenter()){
            this.service.registerNominee(sessionStorage.getItem('userId'), this.area).subscribe((data)=>{
                alert("Registered as a Nominee!"), this.router.navigate(['/user']).then(()=>window.location.reload())
                },error=>{alert(error.error)})
        }
    }
}