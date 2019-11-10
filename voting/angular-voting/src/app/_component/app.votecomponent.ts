import { Component, OnInit } from "@angular/core";
import { UserService } from "../_service/app.userservice";
import { Router } from "@angular/router";
import { Poll } from "../_model/app.poll";
import { User } from "../_model/app.user";

@Component({
    selector:'vote',
    templateUrl:'../_html/app.vote.html'
})
export class VoteComponent implements OnInit{

    center:string;
    poll:Poll={
        pollCenter:null,
        startTime:null,
        endTime:null
    }
    currentTime:any;
    nominees:User;

    constructor(private service:UserService, private router:Router){}

    ngOnInit(){
        this.service.searchUserPoll(sessionStorage.getItem("userEmail")).subscribe(
            (data:Poll)=>{this.poll=data;this.getStartTime();this.getEndTime();this.getNominees()});
        this.center=sessionStorage.getItem("location");
        this.currentTime = new Date().getTime();
        console.log(this.currentTime);
    }

    getStartTime(){
        this.poll.startTime = new Date(this.poll.startTime).getTime();
        console.log(this.poll.startTime);
    }

    getEndTime(){
        this.poll.endTime = new Date(this.poll.endTime).getTime();
        console.log(this.poll.endTime);
    }

    getNominees(){
        this.service.getCenterNominees(sessionStorage.getItem("location")).subscribe((data:User)=>{
            this.nominees=data;console.log(this.nominees);
        })
    }

    vote(id:number){
        this.service.vote(sessionStorage.getItem("userId"),id).subscribe((data)=>
            {alert("Voted Successfully!")}, error=>{alert(error.error)});
    }
}