package info.bitsolucoes.portal.controller;

import info.bitsolucoes.portal.model.Ticket;
import info.bitsolucoes.portal.model.User;
import info.bitsolucoes.portal.repository.CategoryRepository;
import info.bitsolucoes.portal.repository.TicketRepository;
import info.bitsolucoes.portal.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class TicketController {

    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TicketController(TicketRepository ticketRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 1. Dashboard com Indicadores Simples
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalTickets", ticketRepository.count());
        model.addAttribute("openTickets", ticketRepository.countByStatus("ABERTO"));
        model.addAttribute("inProgressTickets", ticketRepository.countByStatus("EM_ATENDIMENTO"));
        model.addAttribute("completedTickets", ticketRepository.countByStatus("CONCLUIDO"));
        return "dashboard";
    }

    // 2. Gerenciamento e Filtros de Solicitações
    @GetMapping("/tickets")
    public String listTickets(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "sortBy", defaultValue = "creationDate") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "DESC") String sortDir,
            Model model) {

        LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        String filterStatus = (status != null && !status.isBlank()) ? status : null;
        String filterTitle = (title != null && !title.isBlank()) ? title : null;

        // Mapeamento dos campos de ordenação válidos para as propriedades das entidades
        String property = switch (sortBy) {
            case "id" -> "id";
            case "title" -> "title";
            case "category" -> "category.name";
            case "user" -> "user.name";
            case "status" -> "status";
            default -> "creationDate";
        };

        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(property).ascending() : Sort.by(property).descending();

        model.addAttribute("tickets", ticketRepository.filterTickets(categoryId, filterStatus, filterTitle, start, end, sort));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("paramCategoryId", categoryId);
        model.addAttribute("paramStatus", filterStatus);
        model.addAttribute("paramTitle", filterTitle);
        model.addAttribute("paramStartDate", startDate);
        model.addAttribute("paramEndDate", endDate);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        return "tickets";
    }

    // 3. Exibir Formulário de Cadastro
    @GetMapping("/tickets/add")
    public String showAddForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("categories", categoryRepository.findAll());
        return "add_ticket";
    }

    // 4. Processar Cadastro da Solicitação
    @PostMapping("/tickets/add")
    public String addTicket(@Valid @ModelAttribute("ticket") Ticket ticket,
                            BindingResult result,
                            Authentication auth,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "add_ticket";
        }

        User currentUser = userRepository.findUserWithRoleByName(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Usuário logado não encontrado no banco."));

        ticket.setUser(currentUser);
        ticket.setStatus("ABERTO");
        ticketRepository.save(ticket);

        redirectAttributes.addFlashAttribute("successMessage", "Solicitação criada com sucesso!");
        return "redirect:/tickets";
    }

    // 5. Exibir Formulário de Edição (Somente solicitações ABERTAS)
    @GetMapping("/tickets/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação inválida ID: " + id));

        if (!"ABERTO".equals(ticket.getStatus())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Apenas solicitações com status ABERTO podem ser editadas.");
            return "redirect:/tickets";
        }

        model.addAttribute("ticket", ticket);
        model.addAttribute("categories", categoryRepository.findAll());
        return "edit_ticket";
    }

    // 6. Processar Edição
    @PostMapping("/tickets/edit/{id}")
    public String updateTicket(@PathVariable("id") Long id,
                               @Valid @ModelAttribute("ticket") Ticket ticketForm,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação inválida ID: " + id));

        if (!"ABERTO".equals(existingTicket.getStatus())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Apenas solicitações com status ABERTO podem ser editadas.");
            return "redirect:/tickets";
        }

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "edit_ticket";
        }

        existingTicket.setTitle(ticketForm.getTitle());
        existingTicket.setDescription(ticketForm.getDescription());
        existingTicket.setCategory(ticketForm.getCategory());

        ticketRepository.save(existingTicket);
        redirectAttributes.addFlashAttribute("successMessage", "Solicitação atualizada com sucesso!");
        return "redirect:/tickets";
    }

    // 7. Excluir Solicitação (Somente solicitações ABERTAS)
    @GetMapping("/tickets/delete/{id}")
    public String deleteTicket(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação inválida ID: " + id));

        if (!"ABERTO".equals(ticket.getStatus())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Apenas solicitações com status ABERTO podem ser excluídas.");
            return "redirect:/tickets";
        }

        ticketRepository.delete(ticket);
        redirectAttributes.addFlashAttribute("successMessage", "Solicitação excluída com sucesso!");
        return "redirect:/tickets";
    }

    // 8. Alterar Status
    @PostMapping("/tickets/status/{id}")
    public String updateStatus(@PathVariable("id") Long id,
                               @RequestParam("status") String newStatus,
                               RedirectAttributes redirectAttributes) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação inválida ID: " + id));

        ticket.setStatus(newStatus);
        ticketRepository.save(ticket);

        redirectAttributes.addFlashAttribute("successMessage", "Status da solicitação #" + id + " alterado para " + newStatus + ".");
        return "redirect:/tickets";
    }
}