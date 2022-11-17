package org.launchcode.techjobs.persistent.controllers;

import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.launchcode.techjobs.persistent.models.dto.JobSkillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private JobRepository jobRepository;

//    JobSkillDTO jobSkill = new JobSkillDTO();

    @RequestMapping("")
    public String index(Model model) {

        model.addAttribute("title", "My Jobs");

        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
        model.addAttribute("title", "Add Job");
        model.addAttribute(new Job());
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());

//        Optional<Job> result = jobRepository.findById(jobId);
//        Job job = result.get();

//        jobSkill.setJob(job);

//        JobSkillDTO jobSkill = new JobSkillDTO();
//
//        model.addAttribute("jobSkill", jobSkill);

        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                    Errors errors, Model model, @RequestParam int employerId, @RequestParam List<Integer> skills) {


        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Job");
            return "add";
        }


        Optional<Employer> selectedEmployer = employerRepository.findById(employerId);
        if (!selectedEmployer.isEmpty()) {
            newJob.setEmployer(selectedEmployer.get());
        } else {
            newJob.setEmployer(new Employer());
        }


        List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
        newJob.setSkills(skillObjs);

        for (Skill skill : skillObjs) {
            JobSkillDTO jobSkill = new JobSkillDTO();
            jobSkill.setSkill(skill);
            jobSkill.setJob(newJob);
        }

        jobRepository.save(newJob);

        return "redirect:";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {
        Optional<Job> result = jobRepository.findById(jobId);

        Job job = result.get();
        model.addAttribute("job", job);

        return "view";
    }


}