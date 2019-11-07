import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import {Routes, RouterModule} from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent }  from './app.component';

import { FormsModule } from '@angular/forms';
import {NgxPaginationModule} from 'ngx-pagination'
 

import {ReactiveFormsModule} from '@angular/forms';
import {  HTTP_INTERCEPTORS } from '@angular/common/http';
import {FileUploadModule} from 'ng2-file-upload';
import {CommonModule} from '@angular/common';
import { Ng2SmartTableModule } from 'ng2-smart-table';
import { Homecomponent } from './_component/app.homecomponent';
import { AdminComponent } from './_component/app.admincomponent';
import { UserComponent } from './_component/app.usercomponent';


const routes:Routes = [
    { path: '', redirectTo: 'home', pathMatch:'full'},
    { path: 'home', component:Homecomponent },
    { path: 'admin', component:AdminComponent },
    { path: 'user', component:UserComponent }
];

@NgModule({
    imports: [
        BrowserModule, FormsModule, HttpClientModule, ReactiveFormsModule, CommonModule, Ng2SmartTableModule, RouterModule.forRoot(routes), FileUploadModule,NgxPaginationModule
    ],
    declarations: [
        AppComponent, Homecomponent, AdminComponent, UserComponent
		], 

    //providers: [{provide:HTTP_INTERCEPTORS, useClass:BasicAuthHtppInterceptorService, multi:true}],

    bootstrap: [AppComponent]
})

export class AppModule { }